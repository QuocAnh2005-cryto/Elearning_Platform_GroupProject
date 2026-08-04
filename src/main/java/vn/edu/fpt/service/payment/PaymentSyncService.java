package vn.edu.fpt.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Payment;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.repository.PaymentRepository;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.PaymentLink;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSyncService {

    private final PaymentRepository paymentRepository;
    private final PayOsService payOsService;
    private final PayOS payOS;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int expirePaymentsByTimeout() {
        try {
            log.info("Bắt đầu kiểm tra các giao dịch thanh toán đã hết hạn...");
            List<Payment> expiredPayments = paymentRepository.findExpiredPendingPayments();
            if (expiredPayments.isEmpty()) {
                log.debug("Không có giao dịch nào hết hạn cần xử lý");
                return 0;
            }
            int count = 0;
            for (Payment payment : expiredPayments) {
                try {
                    payOsService.expirePayment(payment);
                    count++;
                } catch (Exception e) {
                    log.error("Lỗi khi đánh dấu giao dịch ID {} là EXPIRED: {}", payment.getId(), e.getMessage());
                }
            }
            log.info("Hoàn tất: {} giao dịch đã được đánh dấu là EXPIRED", count);
            return count;
        } catch (Exception e) {
            log.error("Lỗi nghiêm trọng trong expirePaymentsByTimeout: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int syncPendingPaymentsFromPayOs() {
        try {
            log.info("Bắt đầu đồng bộ hóa trạng thái PENDING từ PayOS...");
            List<Payment> pendingPayments = paymentRepository.findPendingPaymentsForSync();
            if (pendingPayments.isEmpty()) {
                log.debug("Không có giao dịch PENDING nào cần đồng bộ");
                return 0;
            }
            log.debug("Tìm thấy {} giao dịch PENDING cần đồng bộ từ PayOS", pendingPayments.size());
            int count = 0;
            for (Payment payment : pendingPayments) {
                try {
                    if (syncPaymentStatusWithPayOs(payment)) {
                        count++;
                        log.debug("Cập nhật trạng thái giao dịch ID {} từ PayOS thành công", payment.getId());
                    }
                } catch (Exception e) {
                    log.warn("Lỗi khi đồng bộ giao dịch ID {} từ PayOS: {}", payment.getId(), e.getMessage());
                }
            }
            log.info("Hoàn tất: {} giao dịch đã được cập nhật từ PayOS", count);
            return count;
        } catch (Exception e) {
            log.error("Lỗi nghiêm trọng trong syncPendingPaymentsFromPayOs: {}", e.getMessage(), e);
            return 0;
        }
    }


    private boolean syncPaymentStatusWithPayOs(Payment payment) {
        try {
            long orderCode = Long.parseLong(payment.getGatewayOrderCode());
            PaymentLink paymentInfo = payOS.paymentRequests().get(orderCode);
            if (paymentInfo == null || paymentInfo.getStatus() == null) {
                log.warn("PayOS trả về null cho order code: {}", orderCode);
                return false;
            }
            String payOsStatus = paymentInfo.getStatus().toString();
            log.debug("Trạng thái từ PayOS cho Payment ID {}: {}", payment.getId(), payOsStatus);
            boolean statusChanged = false;
            if ("PAID".equalsIgnoreCase(payOsStatus) && payment.getStatus() != PaymentStatus.PAID) {
                payOsService.completePayment(payment);
                statusChanged = true;
            } else if ("CANCELLED".equalsIgnoreCase(payOsStatus) && payment.getStatus() != PaymentStatus.CANCELLED) {
                payOsService.cancelPayment(payment);
                statusChanged = true;
            } else if ("EXPIRED".equalsIgnoreCase(payOsStatus) && payment.getStatus() != PaymentStatus.EXPIRED) {
                payOsService.expirePayment(payment);
                statusChanged = true;
            } else if ("FAILED".equalsIgnoreCase(payOsStatus) && payment.getStatus() != PaymentStatus.FAILED) {
                payOsService.failPayment(payment);
                statusChanged = true;
            }
            payment.setLastSyncedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            return statusChanged;
        } catch (NumberFormatException e) {
            log.error("Gateway order code không hợp lệ cho Payment ID {}: {}", payment.getId(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi query PayOS cho Payment ID {}: {}", payment.getId(), e.getMessage(), e);
            return false;
        }
    }

}

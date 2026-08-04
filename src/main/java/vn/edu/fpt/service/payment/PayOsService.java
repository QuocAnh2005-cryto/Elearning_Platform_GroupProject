package vn.edu.fpt.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.enums.OrderStatus;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.exception.PaymentCallApiException;
import vn.edu.fpt.exception.PaymentCreateException;
import vn.edu.fpt.repository.*;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.edu.fpt.enums.LogAction;
import vn.edu.fpt.repository.SystemLogRepository;

import vn.edu.fpt.util.AppConstants;
import vn.payos.model.v2.paymentRequests.PaymentLinkStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PayOsService {

    private final PayOS payOS;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SystemLogRepository systemLogRepository;

    public Payment createPaymentOrder(Order order, String returnUrl, String cancelUrl) {
        try {
            log.info("Đang tạo đơn hàng thanh toán cổng {} cho Order ID: {}", AppConstants.PAYMENT_GATEWAY, order.getId());

            long amountVND = order.getTotalAmount().longValue();
            long orderCode = System.currentTimeMillis() / 1000;
            long expirationTimeUnix = (System.currentTimeMillis() / 1000) + (AppConstants.PAYMENT_EXPIRATION_MINUTES * 60L);

            CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(amountVND)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .description(String.valueOf(orderCode))
                    .buyerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                    .buyerEmail(order.getUser().getEmail())
                    .buyerPhone(order.getUser().getPhone() != null ? order.getUser().getPhone() : "")
                    .expiredAt(expirationTimeUnix)
                    .build();

            CreatePaymentLinkResponse response;
            try {
                response = payOS.paymentRequests().create(request);
            } catch (PayOSException e) {
                log.error("{} API error: {}", AppConstants.PAYMENT_GATEWAY, e.getMessage());
                throw new PaymentCallApiException("Không thể gọi tới hệ thống PayOs", e);
            }

            log.info("Tạo liên kết thanh toán thành công: {}", response.toString());
            log.info("Tên tài khoản thụ hưởng: {}", response.getAccountName());
            log.info("Mã QR: {}", response.getQrCode());
            log.info("Đường dẫn thanh toán: {}", response.getCheckoutUrl());
            log.info("Số tài khoản thụ hưởng: {}", response.getAccountNumber());

            String qrCodeUrl = response.getQrCode();
            String checkoutUrl = response.getCheckoutUrl();
            String accountNumber = response.getAccountNumber();
            String bankName = AppConstants.BANK_NAMES.get(response.getBin());
            String accountHolder = response.getAccountName();
            String description = String.valueOf(orderCode);

            if (qrCodeUrl == null || qrCodeUrl.isEmpty()) {
                log.warn("QR Code URL từ cổng thanh toán rỗng hoặc null.");
            }
            if (checkoutUrl == null || checkoutUrl.isEmpty()) {
                log.warn("Checkout URL từ cổng thanh toán rỗng hoặc null.");
            }

            Payment payment = Payment.builder()
                    .order(order)
                    .gateway(AppConstants.PAYMENT_GATEWAY)
                    .amount(BigDecimal.valueOf(amountVND))
                    .status(PaymentStatus.PENDING)
                    .gatewayOrderCode(String.valueOf(orderCode))
                    .paymentUrl(checkoutUrl)
                    .qrCodeUrl(qrCodeUrl)
                    .accountNumber(accountNumber)
                    .description(description)
                    .bankName(bankName)
                    .accountHolder(accountHolder)
                    .expiredAt(LocalDateTime.now().plusMinutes(AppConstants.PAYMENT_EXPIRATION_MINUTES))
                    .build();

            Payment savedPayment = paymentRepository.save(payment);
            saveSystemLog(savedPayment, LogAction.CREATE_PAYMENT, String.format(
                "{\"amount\": %s, \"orderId\": %d, \"gatewayOrderCode\": \"%s\"}",
                savedPayment.getAmount().toString(),
                order.getId(),
                savedPayment.getGatewayOrderCode()
            ));

            return savedPayment;

        } catch (Exception e) {
            log.error("Lỗi khi tạo đơn hàng PayOS", e);
            throw new PaymentCreateException("Lỗi khi khởi tạo thanh toán: " + e);
        }
    }

    public void completePayment(Payment payment) {
        if (payment.getStatus() == PaymentStatus.PAID) {
            return;
        }

        log.info("Hoàn tất thanh toán cho Payment ID: {}, Mã đơn hàng cổng: {}", payment.getId(), payment.getGatewayOrderCode());
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        saveSystemLog(payment, LogAction.PAYMENT_COMPLETED, String.format(
            "{\"amount\": %s, \"orderId\": %d, \"gatewayOrderCode\": \"%s\"}",
            payment.getAmount().toString(),
            payment.getOrder() != null ? payment.getOrder().getId() : 0,
            payment.getGatewayOrderCode()
        ));

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            User user = order.getUser();
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElse(null);

                for (OrderItem item : order.getItems()) {
                    Course course = item.getCourse();
                    if (course != null) {
                        boolean alreadyEnrolled = enrollmentRepository.existsByUserAndCourse(user, course);
                        if (!alreadyEnrolled) {
                            Enrollment enrollment = Enrollment.builder()
                                    .user(user)
                                    .course(course)
                                    .progressPercent(BigDecimal.ZERO)
                                    .build();
                            enrollmentRepository.save(enrollment);
                            log.info("Ghi danh thành công cho người dùng {} vào khóa học {}", user.getEmail(), course.getTitle());
                        }

                        if (cart != null && cart.getItems() != null) {
                            cart.getItems().removeIf(ci -> ci.getCourse().getId().equals(course.getId()));
                        }
                    }
                }

                if (cart != null) {
                    cartRepository.save(cart);
                }
            }
        }
    }

    public void cancelPaymentAndInvalidatePayOs(Payment payment) {
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            return;
        }
        log.info("Yêu cầu hủy link thanh toán phía cổng PayOS cho Payment ID: {}", payment.getId());
        try {
            long orderCode = Long.parseLong(payment.getGatewayOrderCode());
            try {
                payOS.paymentRequests().cancel(orderCode, "Người dùng yêu cầu hủy giao dịch");
                log.info("Hủy thành công liên kết thanh toán trên PayOS cho mã đơn hàng: {}", orderCode);
            } catch (Exception e) {
                log.warn("Gọi cổng PayOS hủy đơn hàng thất bại (có thể đơn hàng đã quá hạn hoặc đã hủy trước đó): {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("Lỗi khi phân tích mã đơn hàng {}: {}", payment.getGatewayOrderCode(), e.getMessage());
        }
        cancelPayment(payment);
    }

    public void cancelPayment(Payment payment) {
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            return;
        }
        log.info("Đang hủy cục bộ giao dịch ID: {}", payment.getId());
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);

        saveSystemLog(payment, LogAction.CANCEL_PAYMENT, String.format(
            "{\"amount\": %s, \"orderId\": %d, \"gatewayOrderCode\": \"%s\"}",
            payment.getAmount().toString(),
            payment.getOrder() != null ? payment.getOrder().getId() : 0,
            payment.getGatewayOrderCode()
        ));

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

    public void expirePayment(Payment payment) {
        if (payment.getStatus() == PaymentStatus.EXPIRED) {
            return;
        }
        log.info("Đang hết hạn giao dịch ID: {}", payment.getId());
        payment.setStatus(PaymentStatus.EXPIRED);
        paymentRepository.save(payment);

        saveSystemLog(payment, LogAction.EXPIRE_PAYMENT, String.format(
            "{\"amount\": %s, \"orderId\": %d, \"gatewayOrderCode\": \"%s\"}",
            payment.getAmount().toString(),
            payment.getOrder() != null ? payment.getOrder().getId() : 0,
            payment.getGatewayOrderCode()
        ));

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.EXPIRED);
            orderRepository.save(order);
        }
    }

    public void failPayment(Payment payment) {
        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }
        log.info("Đang đánh dấu thất bại giao dịch ID: {}", payment.getId());
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

    private void saveSystemLog(Payment payment, LogAction action, String meta) {
        try {
            SystemLog systemLog = SystemLog.builder()
                    .user(payment.getOrder().getUser())
                    .action(action)
                    .targetType("PAYMENT")
                    .targetId(String.valueOf(payment.getId()))
                    .meta(meta)
                    .createdAt(LocalDateTime.now())
                    .build();
            systemLogRepository.save(systemLog);
        } catch (Exception e) {
            log.error("Lỗi khi ghi system log cho giao dịch ID {}: {}", payment.getId(), e.getMessage());
        }
    }

    public String getPaymentStatusByOrderCode(String orderCode) {
        PaymentLinkStatus status = payOS.paymentRequests().get(orderCode).getStatus();
        return status.name();
    }
}

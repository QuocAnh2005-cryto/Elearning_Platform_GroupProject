package vn.edu.fpt.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.fpt.service.payment.PaymentSyncService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduledTasks {

    private final PaymentSyncService paymentSyncService;

    @Scheduled(fixedRateString = "#{T(vn.edu.fpt.util.AppConstants).EXPIRED_PAYMENT_CHECK_INTERVAL_SECONDS * 1000}")
    public void expirePaymentsByTimeout() {
        try {
            log.debug("[SCHEDULED TASK] Bắt đầu expirePaymentsByTimeout");
            int expiredCount = paymentSyncService.expirePaymentsByTimeout();
            log.debug("[SCHEDULED TASK] Hoàn tất expirePaymentsByTimeout: {} giao dịch đã expire", expiredCount);
        } catch (Exception e) {
            log.error("[SCHEDULED TASK] Lỗi trong expirePaymentsByTimeout: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRateString = "#{T(vn.edu.fpt.util.AppConstants).PENDING_SYNC_CHECK_INTERVAL_MINUTES * 60 * 1000}")
    public void syncPendingPaymentsFromPayOs() {
        try {
            log.debug("[SCHEDULED TASK] Bắt đầu syncPendingPaymentsFromPayOs");
            int syncedCount = paymentSyncService.syncPendingPaymentsFromPayOs();
            log.debug("[SCHEDULED TASK] Hoàn tất syncPendingPaymentsFromPayOs: {} giao dịch đã sync", syncedCount);
        } catch (Exception e) {
            log.error("[SCHEDULED TASK] Lỗi trong syncPendingPaymentsFromPayOs: {}", e.getMessage(), e);
        }
    }

}

package vn.edu.fpt.controller.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.entity.Payment;
import vn.edu.fpt.service.payment.PaymentService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout() {
        log.info("Nhận yêu cầu checkout từ giỏ hàng.");
        Payment payment = paymentService.checkout();
        Map<String, Object> response = new HashMap<>();
        response.put("id", payment.getId());
        response.put("orderId", payment.getOrder().getId());
        response.put("amount", payment.getAmount());
        response.put("gatewayOrderCode", payment.getGatewayOrderCode());
        response.put("status", payment.getStatus());
        response.put("paymentUrl", payment.getPaymentUrl());
        response.put("qrCodeUrl", payment.getQrCodeUrl());
        response.put("expiredAt", payment.getExpiredAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Integer paymentId) {
        log.info("Nhận yêu cầu kiểm tra trạng thái cho Payment ID: {}", paymentId);
        Payment payment = paymentService.getPaymentStatus(paymentId);
        Map<String, Object> response = new HashMap<>();
        response.put("id", payment.getId());
        response.put("status", payment.getStatus());
        response.put("amount", payment.getAmount());
        response.put("orderId", payment.getOrder().getId());
        response.put("paidAt", payment.getPaidAt());
        response.put("expiredAt", payment.getExpiredAt());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Integer paymentId) {
        log.info("Nhận yêu cầu hủy thanh toán thủ công cho Payment ID: {}", paymentId);
        paymentService.cancelPaymentManually(paymentId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Hủy giao dịch thành công.");
        return ResponseEntity.ok(response);
    }
}

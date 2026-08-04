package vn.edu.fpt.controller.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.service.payment.PaymentService;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentPageController {

    private final PaymentService paymentService;

    @GetMapping("/payment")
    public String paymentPage(@RequestParam(value = "id", required = false) Integer paymentId, Model model) {
        log.info("Loading payment page with paymentId: {}", paymentId);
        Map<String, Object> paymentData = paymentService.getPaymentPageData(paymentId);
        model.addAttribute("paymentData", paymentData);
        return "payment/payment";
    }
}

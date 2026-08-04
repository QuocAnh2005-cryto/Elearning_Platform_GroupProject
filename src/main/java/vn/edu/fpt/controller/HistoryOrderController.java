package vn.edu.fpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.edu.fpt.dto.user.StudentPurchaseHistoryDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.HistoryOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HistoryOrderController {

    private final UserRepository userRepository;
    private final HistoryOrderService historyOrderService;

    public HistoryOrderController(UserRepository userRepository, HistoryOrderService historyOrderService) {
        this.userRepository = userRepository;
        this.historyOrderService = historyOrderService;
    }

    private User getSessionUser() {
        return vn.edu.fpt.util.SecurityUtils.getCurrentUser();
    }

    @GetMapping("/student/purchase-history")
    public String showPurchaseHistory(
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            Model model) {
        User user = getSessionUser();
        if (user == null) {
            return "redirect:/login_no";
        }
        
        StudentPurchaseHistoryDto purchaseHistoryData = historyOrderService.getPurchaseHistoryData(user, status);
        
        model.addAttribute("currentUser", purchaseHistoryData.getCurrentUser());
        model.addAttribute("orders", purchaseHistoryData.getOrders());
        model.addAttribute("status", status);
        
        return "purchase_history/purchase_history";
    }
}

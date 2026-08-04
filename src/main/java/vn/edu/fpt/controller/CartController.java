package vn.edu.fpt.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.CartService;
import vn.edu.fpt.dto.cart.CartPageDetailsDto;

import java.util.HashMap;
import java.util.Map;

@Controller
@Transactional
public class    CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        return vn.edu.fpt.util.SecurityUtils.getCurrentUser();
    }

    @Transactional
    @GetMapping("/cart")
    public String showCartPage(Model model) {
        User user = getAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        CartPageDetailsDto details = cartService.getCartPageDetails(user);

        model.addAttribute("cart", details.getCart());
        model.addAttribute("itemsByInstructor", details.getItemsByInstructor());
        model.addAttribute("cartSize", details.getCartSize());
        model.addAttribute("subtotal", details.getSubtotal());
        model.addAttribute("total", details.getTotal());
        model.addAttribute("selectedItemsCount", details.getSelectedItemsCount());
        model.addAttribute("instructorCheckboxState", details.getInstructorCheckboxState());
        model.addAttribute("globalCheckboxState", details.getGlobalCheckboxState());

        return "cart/cart";
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCourseToCart(@RequestParam("courseId") Integer courseId) {
        try {
            User user = getAuthenticatedUser();
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để thực hiện.");
                return ResponseEntity.status(401).body(response);
            }
            Map<String, Object> response = cartService.addCourseToCart(user, courseId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeCartItem(@RequestParam("cartItemId") Integer cartItemId) {
        Map<String, Object> response = new HashMap<>();
        try {
            cartService.removeCartItem(cartItemId);
            response.put("success", true);
            response.put("message", "Đã xóa khóa học khỏi giỏ hàng.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi xóa: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/cart/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = getAuthenticatedUser();
            if (user == null) {
                response.put("success", false);
                response.put("cartSize", 0);
                return ResponseEntity.ok(response);
            }
            int cartSize = cartService.getCartCount(user);
            response.put("success", true);
            response.put("cartSize", cartSize);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("cartSize", 0);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/api/cart/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkoutCart() {
        try {
            User user = getAuthenticatedUser();
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để thực hiện.");
                return ResponseEntity.status(401).body(response);
            }
            Map<String, Object> response = cartService.checkoutCart(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thanh toán thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/cart/toggle-select")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleSelect(@RequestParam("cartItemId") Integer cartItemId,
                                                            @RequestParam(value = "selected", required = false) Boolean selected) {
        Map<String, Object> response = new HashMap<>();
        try {
            cartService.toggleSelect(cartItemId, selected);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/cart/toggle-select-instructor")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleSelectInstructor(@RequestParam("instructorId") Integer instructorId,
                                                                      @RequestParam("selected") Boolean selected) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = getAuthenticatedUser();
            if (user == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập.");
                return ResponseEntity.status(401).body(response);
            }
            cartService.toggleSelectInstructor(user, instructorId, selected);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/api/cart/toggle-select-all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleSelectAll(@RequestParam("selected") Boolean selected) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = getAuthenticatedUser();
            if (user == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập.");
                return ResponseEntity.status(401).body(response);
            }
            cartService.toggleSelectAll(user, selected);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
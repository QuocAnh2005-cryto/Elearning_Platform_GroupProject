package vn.edu.fpt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.LoginRequest;
import vn.edu.fpt.dto.RegisterRequest;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.AuthService;

import java.util.Collections;
import java.util.List;

@Controller
@SessionAttributes("registerRequest")
public class AuthController {
    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "authRequired", required = false) String authRequired) {

        model.addAttribute(
                "loginRequest",
                new LoginRequest());
        
        if (authRequired != null) {
            model.addAttribute("errorMessage", "Bạn cần login để thực hiện được thao tác này");
            model.addAttribute("toastType", "error");
        }
        
        System.out.println("LOGIN HIT");
        return "auth/login";
    }


    @GetMapping("/register")
    public String registerPage(
            Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest());

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(

            @Valid
            @ModelAttribute("registerRequest")
            RegisterRequest request,
            BindingResult result,
            Model model,
            HttpSession session) {

        if(result.hasErrors()) {
            return "auth/register";
        }


        if (authService.isActiveEmail(request.getEmail())) {
            result.rejectValue(
                    "email",
                    "duplicate",
                    "Email đã tồn tại");
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank() && authService.isActivePhone(request.getPhoneNumber())) {
            result.rejectValue(
                    "phoneNumber",
                    "duplicate",
                    "Số điện thoại đã tồn tại");
        }

        if (!request.isPasswordMatched()) {
            result.rejectValue(
                    "confirmPassword",
                    "mismatch",
                    "Mật khẩu xác nhận không khớp");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        User user = authService.register(request);

        session.setAttribute("VERIFY_USER_ID", user.getId());

        model.addAttribute("showOtp", true);

        return "auth/register";

    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam("otpCode")
            String otp,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("VERIFY OTP HIT");
        Integer userId = (Integer) session.getAttribute("VERIFY_USER_ID");

        try {

            authService.verifyOtp(userId, otp);
            session.removeAttribute("VERIFY_USER_ID");
            System.out.println("THÀNH CÔNGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG !");
            redirectAttributes.addFlashAttribute("toastMessage", "Đăng kí tài khoản thành công !");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/login";

        }

        catch (Exception ex) {

            model.addAttribute("showOtp", true);

            model.addAttribute("otpError", ex.getMessage());
            System.out.println("THÀNH THẤT BẠIIIIIIIIIIIIIIIIIIIIIIIIIIIIII !");

            return "auth/register";

        }

    }

    @PostMapping("/resend-otp")
    public String resendOtp(
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("VERIFY_USER_ID");

        if (userId == null) {

            return "redirect:/register";

        }

        try {

            authService.resendOtp(userId);

            model.addAttribute("showOtp", true);
            model.addAttribute("otpSuccess", "OTP mới đã được gửi");


        }

        catch (Exception ex) {

            model.addAttribute("showOtp", true);
            model.addAttribute("otpError", ex.getMessage());
        }
        return "auth/register";

    }

    @GetMapping("/view-current-role")
    @ResponseBody
    public String debug(Authentication authentication) {

        authentication.getAuthorities()
                .forEach(System.out::println);

        return authentication.getAuthorities().toString();
    }
}

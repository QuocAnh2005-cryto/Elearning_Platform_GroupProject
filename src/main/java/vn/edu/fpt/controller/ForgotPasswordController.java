package vn.edu.fpt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.ForgotPasswordRequestDto;
import vn.edu.fpt.dto.ResetPasswordRequestDto;
import vn.edu.fpt.service.AuthService;
import vn.edu.fpt.service.PasswordResetService;
import vn.edu.fpt.service.UserService;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;
    private final AuthService authService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {

        model.addAttribute("forgotPasswordRequest",
                new ForgotPasswordRequestDto());

        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDto request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/forgot-password";
        }



        if (!authService.existsByEmail(request.getEmail())) {

            result.rejectValue(
                    "email",
                    "notFound",
                    "Email không tồn tại trong hệ thống");

            return "auth/forgot-password";
        }

        passwordResetService.sendResetLink(request.getEmail());

        redirectAttributes.addFlashAttribute(
                "message",
                "Link đặt lại mật khẩu đã được gửi tới email của bạn"
        );

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam String token,
            Model model) {

        if(passwordResetService.validateToken(token) == null){
            return "auth/invalid-token";
        }

        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setToken(token);

        model.addAttribute("resetPasswordRequest", request);

        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @Valid @ModelAttribute("resetPasswordRequest") ResetPasswordRequestDto request,
            BindingResult result) {

        if (result.hasErrors()) {
            return "auth/reset-password";
        }

        if (!request.isPasswordMatched()) {
            result.rejectValue(
                    "confirmPassword",
                    "mismatch",
                    "Mật khẩu xác nhận không khớp");
        }

        if (result.hasErrors()) {
            return "auth/reset-password";
        }



        passwordResetService.resetPassword(request);

        return "auth/changepass-success";
    }

}

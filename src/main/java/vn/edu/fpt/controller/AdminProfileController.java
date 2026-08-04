package vn.edu.fpt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.util.SecurityUtils;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AzureBlobService azureBlobService;

    @GetMapping
    public String showProfile(Model model) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(currentUser.getId()).orElse(currentUser);
        model.addAttribute("user", user);
        return "admin/profile/profile";
    }

    @GetMapping("/change-password")
    public String showChangePassword(Model model) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(currentUser.getId()).orElse(currentUser);
        model.addAttribute("user", user);
        return "admin/profile/change-password";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("phone") String phone,
            @RequestParam("bio") String bio,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(currentUser.getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin tài khoản.");
            return "redirect:/admin/profile";
        }

        // Xác thực Số điện thoại
        if (phone != null && !phone.isBlank()) {
            User existingPhoneUser = userRepository.findUserByPhone(phone.trim());
            if (existingPhoneUser != null && !existingPhoneUser.getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Số điện thoại này đã được sử dụng bởi tài khoản khác.");
                return "redirect:/admin/profile";
            }
            user.setPhone(phone.trim());
        }

        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setBio(bio != null ? bio.trim() : "");
        user.setUpdatedAt(LocalDateTime.now());

        // Xử lý ảnh đại diện nếu có tải lên
        if (avatarFile != null && !avatarFile.isEmpty()) {
            if (avatarFile.getSize() > 2 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("error", "Kích thước ảnh đại diện không được vượt quá 2MB.");
                return "redirect:/admin/profile";
            }
            try {
                String url = azureBlobService.saveFile(avatarFile, "user-avatars");
                user.setAvatarUrl(url);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh đại diện: " + e.getMessage());
                return "redirect:/admin/profile";
            }
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        return "redirect:/admin/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(currentUser.getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin tài khoản.");
            return "redirect:/admin/profile/change-password";
        }

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ tất cả các trường mật khẩu.");
            return "redirect:/admin/profile/change-password";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không chính xác.");
            return "redirect:/admin/profile/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            return "redirect:/admin/profile/change-password";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return "redirect:/admin/profile/change-password";
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công!");
        return "redirect:/admin/profile/change-password";
    }
}

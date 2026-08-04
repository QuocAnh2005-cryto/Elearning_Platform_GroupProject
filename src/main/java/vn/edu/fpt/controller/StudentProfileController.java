package vn.edu.fpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.user.StudentProfileDashboardDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.StudentProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.util.SecurityUtils;

@Controller
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    private User getSessionUser() {
        return SecurityUtils.getCurrentUser();
    }


    @GetMapping("/student/profile")
    public String showStudentProfile(Model model) {
        User user = getSessionUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        StudentProfileDashboardDto dashboardData = studentProfileService.getDashboardData(user);

        model.addAttribute("currentUser", dashboardData.getCurrentUser());
        model.addAttribute("enrollmentsCount", dashboardData.getEnrollmentsCount());
        model.addAttribute("certificatesCount", dashboardData.getCertificatesCount());
        model.addAttribute("studyHours", dashboardData.getStudyHours());
        model.addAttribute("lessonNotes", dashboardData.getLessonNotes());
        
        return "student_profile/student_profile";
    }

    @PostMapping("/student/profile/update")
    public String updateStudentProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam(value = "deleteAvatar", required = false, defaultValue = "false") boolean deleteAvatar,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        User user = getSessionUser();
        if (user == null) {
            return "redirect:/login";
        }

        try {
            User updatedUser = studentProfileService.updateProfile(user, firstName, lastName, email, phone, avatarFile, deleteAvatar);
            
            // Update session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute("user", updatedUser);
            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin hồ sơ tài khoản thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student/profile";
    }


    @PostMapping("/student/profile/change-password")
    public String changePassword(
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User user = getSessionUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            User updatedUser = studentProfileService.changePassword(user, oldPassword, newPassword, confirmPassword);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute("user", updatedUser);
            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống.");
        }

        return "redirect:/student/profile";
    }

}

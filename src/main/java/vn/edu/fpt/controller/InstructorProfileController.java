package vn.edu.fpt.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.user.InstructorProfileViewDto;
import vn.edu.fpt.dto.user.ProfileDto;
import vn.edu.fpt.exception.UserValidationException;
import vn.edu.fpt.service.InstructorProfileService;

@RequestMapping("/instructor")
@Controller
public class InstructorProfileController {

    private final InstructorProfileService instructorProfileService;

    public InstructorProfileController(InstructorProfileService instructorProfileService) {
        this.instructorProfileService = instructorProfileService;
    }

    @GetMapping("/sidebar")
    public String viewProfile(Model model) {
        addProfileAttributes(model, instructorProfileService.getCurrentInstructorProfile(false));
        return "instructor_course/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        addProfileAttributes(model, instructorProfileService.getCurrentInstructorProfile(true));
        return "instructor_course/profile";
    }

    @PostMapping("/profiles")
    public String updateProfile(
            @Valid @ModelAttribute("instructor") ProfileDto profileDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addProfileAttributes(model, instructorProfileService.getCurrentInstructorProfileForInvalidForm(profileDto));
            model.addAttribute("error", "Vui lòng kiểm tra thông tin chưa hợp lệ!.");
            return "instructor_course/profile";
        }

        try {
            instructorProfileService.updateCurrentInstructorProfile(profileDto);
            redirectAttributes.addFlashAttribute("success", "Thay đổi thành công!");
            return "redirect:/instructor/sidebar";
        } catch (UserValidationException e) {
            result.rejectValue(e.getField(), "error", e.getMessage());
            addProfileAttributes(model, instructorProfileService.getCurrentInstructorProfileForInvalidForm(profileDto));
            model.addAttribute("error", e.getMessage());
            return "instructor_course/profile";
        }
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        try {
            instructorProfileService.updateCurrentInstructorPassword(oldPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống khi đổi mật khẩu.");
        }

        return "redirect:/instructor/sidebar";
    }

    private void addProfileAttributes(Model model, InstructorProfileViewDto profileView) {
        model.addAttribute("instructor", profileView.getInstructor());
        model.addAttribute("averageRating", profileView.getAverageRating());
        model.addAttribute("ratingStars", profileView.getRatingStars());
        model.addAttribute("ratingCount", profileView.getRatingCount());
        model.addAttribute("editMode", profileView.isEditMode());
    }
}

package vn.edu.fpt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.service.AuthService;
import vn.edu.fpt.service.UserService;
import vn.edu.fpt.util.AppConstants;

import java.util.List;

@Controller
@RequestMapping("/manager/instructor")
public class ManagerInstructorController {

    private final UserService userService;
    private final AuthService authService;

    public ManagerInstructorController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    // List instructors.
    @GetMapping("/list")
    public String listInstructors(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserDto> requestPage = userService.searchAndFilterInstructors(keyword, status, pageable);

        int startPage = 0;
        int endPage = 0;
        if (requestPage.getTotalPages() > 0) {
            startPage = (requestPage.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
            endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, requestPage.getTotalPages() - 1);
        }

        model.addAttribute("requestPage", requestPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "manager/approval-instructor/instructor-list";
    }

    // instructor detail.
    @GetMapping("/detail/{id}")
    public String detailInstructor(@PathVariable Integer id, Model model) {
        UserDto request = userService.getInstructorDetail(id);
        List<CourseDto> courses = userService.getInstructorCourses(id);

        model.addAttribute("request", request);
        model.addAttribute("courses", courses);
        model.addAttribute("statuses", UserStatus.values());
        return "manager/approval-instructor/instructor-detail";
    }

    // Update instructor status.
    @PostMapping("/edit/{id}")
    public String updateInstructorStatus(
            @PathVariable Integer id,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateInstructorStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản giảng viên thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/manager/instructor/detail/" + id;
    }

    // Create instructor account.
    @PostMapping("/create")
    public String createInstructor(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            RedirectAttributes redirectAttributes) {

        try {
            authService.createInstructorAccount(firstName, lastName, email, phone);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm giảng viên thành công! Thông tin đăng nhập đã được gửi vào email của giảng viên.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/manager/instructor/list";
    }
}

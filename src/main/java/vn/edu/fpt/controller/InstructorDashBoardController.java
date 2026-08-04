package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.dto.revenueInstructor.DashboardInstructorDto;
import vn.edu.fpt.dto.revenueInstructor.RecentOrderDto;
import vn.edu.fpt.dto.user.ProfileDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.DashboardInstructorService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorDashBoardController {
    private final DashboardInstructorService service;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "MONTH") String period,
                            @RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {

        User currentUser = SecurityUtils.getCurrentUser();

        DashboardInstructorDto stats = service.getStats(currentUser.getId(), period, year, month);

        model.addAttribute("instructor", toProfileDto(currentUser));
        model.addAttribute("stats", stats);
        model.addAttribute("selectedPeriod", period);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        return "instructor_course/dashboard";
    }

    private String courseThumbnailBaseUrl() {
        return AppConstants.AZURE_STORAGE_BASE_URL + "/" + AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/";
    }

    private ProfileDto toProfileDto(User user) {
        ProfileDto instructor = new ProfileDto();
        instructor.setFirstname(user.getFirstName());
        instructor.setLastname(user.getLastName());
        instructor.setEmail(user.getEmail());
        instructor.setBio(user.getBio());
        instructor.setAvatar_url(user.getAvatarUrl());
        instructor.setPhone(user.getPhone());
        return instructor;
    }
}

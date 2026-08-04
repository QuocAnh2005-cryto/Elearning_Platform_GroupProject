package vn.edu.fpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.edu.fpt.dto.user.StudentLearningDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.MyCourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyCourseController {

    private final UserRepository userRepository;
    private final MyCourseService myCourseService;

    public MyCourseController(UserRepository userRepository, MyCourseService myCourseService) {
        this.userRepository = userRepository;
        this.myCourseService = myCourseService;
    }

    private User getSessionUser() {
        return vn.edu.fpt.util.SecurityUtils.getCurrentUser();
    }

    @GetMapping("/student/my-learning")
    public String showMyLearning(
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        User user = getSessionUser();
        if (user == null) {
            return "redirect:/login_no";
        }
        
        StudentLearningDto learningData = myCourseService.getLearningData(user, filter, page);
        
        model.addAttribute("currentUser", learningData.getCurrentUser());
        model.addAttribute("enrollments", learningData.getEnrollments());
        model.addAttribute("enrollmentsCount", learningData.getEnrollmentsCount());
        model.addAttribute("filter", learningData.getFilter());
        model.addAttribute("currentPage", learningData.getCurrentPage());
        model.addAttribute("totalPages", learningData.getTotalPages());
        
        return "my_learning/my_learning";
    }
}

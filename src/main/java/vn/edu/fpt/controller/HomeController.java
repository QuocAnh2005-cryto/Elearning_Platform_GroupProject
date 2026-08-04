package vn.edu.fpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.edu.fpt.dto.home.HomeDto;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import vn.edu.fpt.enums.RoleType;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final CourseService courseService;

    public HomeController(UserRepository userRepository, CourseService courseService) {
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    private User getSessionUser() {
        return vn.edu.fpt.util.SecurityUtils.getCurrentUser();
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        User currentUser = getSessionUser();
        if (currentUser != null && currentUser.getRoles() != null) {
            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleType.ADMIN);
            boolean isManager = currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleType.MANAGER);
            boolean isInstructor = currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleType.INSTRUCTOR);

            if (isAdmin) {
                return "redirect:/admin/dashboard";
            }
            if (isManager) {
                return "redirect:/manager/dashboard";
            }
            if (isInstructor) {
                return "redirect:/instructor/dashboard";
            }

            if (!currentUser.isFavoriteSetupCompleted()) {
                return "redirect:/student/favorites/step1";
            }
        }
        HomeDto homeData = courseService.getHomeData(currentUser);
        model.addAttribute("homeData", homeData);
        return "home/home";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }
}


package vn.edu.fpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.fpt.dto.instructor.InstructorPublicProfileDto;
import vn.edu.fpt.service.InstructorPublicProfileService;

@Controller
public class InstructorPublicProfileController {

    private final InstructorPublicProfileService instructorPublicProfileService;

    public InstructorPublicProfileController(InstructorPublicProfileService instructorPublicProfileService) {
        this.instructorPublicProfileService = instructorPublicProfileService;
    }

    @GetMapping("/instructor-profile/{id}")
    public String showInstructorProfile(@PathVariable("id") Integer id, Model model) {
        InstructorPublicProfileDto profile = instructorPublicProfileService.getInstructorProfile(id);
        model.addAttribute("instructor", profile);
        return "instructor/instructor-profile";
    }
}

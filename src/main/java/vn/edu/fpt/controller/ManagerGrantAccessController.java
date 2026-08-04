package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.fpt.dto.course.CourseGrantDTO;
import vn.edu.fpt.dto.user.LearnerInfomationGrantAccessDTO;
import vn.edu.fpt.enums.EnrollmentGrantReason;
import vn.edu.fpt.service.CourseService;
import vn.edu.fpt.service.EnrollmentService;
import vn.edu.fpt.service.UserService;
import vn.edu.fpt.util.AppConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ManagerGrantAccessController {

    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/manager/grant-access")
    public String managerGrantAccess(Model model, @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {

        Page<LearnerInfomationGrantAccessDTO> learners = userService.findAllLearnerByFilter(keyword, page);
        Integer startPage = (learners.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK)
                * AppConstants.NUMBER_PAGE_PER_BLOCK;
        Integer endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, learners.getTotalPages() - 1);

        List<CourseGrantDTO> courses = courseService.findAllCourseGrant();

        model.addAttribute("reasons", EnrollmentGrantReason.values());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("learners", learners);
        model.addAttribute("courses", courses);
        return "manager/grant-access/grant-access";
    }

    @GetMapping("/manager/grant-access/available-courses")
    @ResponseBody
    public ResponseEntity<List<CourseGrantDTO>> getAvailableCoursesForUser(@RequestParam("userId") Integer userId) {
        List<CourseGrantDTO> courses = courseService.findAvailableCoursesForUser(userId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/manager/grant-access")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> grantAccessLearnerToCourse(@RequestParam("userId") Integer userId,
            @RequestParam("courseId") List<Integer> courseIds, @RequestParam("reason") String reason,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "sendEmail", required = false) Boolean sendEmail) {
        Map<String, Object> map = new HashMap<>();
        try {
            String message = enrollmentService.grantAccessCourses(userId, courseIds, reason, note, sendEmail);
            map.put("success", true);
            map.put("message", message);
        } catch (Exception e) {
            map.put("success", false);
            map.put("error", e.getMessage());
            map.put("message", e.getMessage());
        }
        return ResponseEntity.ok(map);
    }
}

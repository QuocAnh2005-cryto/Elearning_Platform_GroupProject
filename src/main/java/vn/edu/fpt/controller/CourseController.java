package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.course.CategoryDto;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.dto.course.CourseListDto;
import vn.edu.fpt.entity.Category;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.RoleType;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.entity.Feedback;
import org.springframework.data.domain.Page;
import vn.edu.fpt.service.CourseService;
import vn.edu.fpt.service.CategoryService;
import vn.edu.fpt.service.UserService;
import vn.edu.fpt.service.EnrollmentService;
import vn.edu.fpt.service.CartService;
import vn.edu.fpt.service.CartItemService;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.util.SecurityUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final EnrollmentService enrollmentService;

    private User getSessionUser() {
        return SecurityUtils.getCurrentUser();
    }

    @GetMapping("/courses")
    public String showCourseList(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "ratings", required = false) List<Double> ratings,
            @RequestParam(value = "prices", required = false) List<String> prices,
            @RequestParam(value = "sort", required = false, defaultValue = "rating") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            Model model) {

        Page<CourseListDto> coursePage = courseService.getPagedCoursesSummary(
                search, categoryId, ratings, prices, sort, page, 4);
        List<CategoryDto> categoryDtos = categoryService.getActiveParentCategories();

        model.addAttribute("parentCategories", categoryDtos);
        User user = getSessionUser();
        Set<Integer> enrolledCourseIds = enrollmentService.getEnrolledCourseIds(user);
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        if (categoryId != null) {
            try {
                Category selectedCategory = categoryService.findByIdAndStatus(categoryId, "ACTIVE");
                if (selectedCategory != null) {
                    model.addAttribute("selectedCategoryName", selectedCategory.getName());
                }
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("selectedRatings", ratings);
        model.addAttribute("selectedPrices", prices);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", coursePage.getNumber() + 1);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalCourses", coursePage.getTotalElements());

        return "course/list";
    }

    @GetMapping("/coursemanager")
    public String getall() {
        return "instructor_course/course_manager";
    }

    @GetMapping("/course/detail")
    public String showCourseDetail(@RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        CourseDto courseDto = courseService.getCourseDetail(id);
        User user = getSessionUser();

        boolean hasAccess = false;
        if (CourseStatus.PUBLISHED.equals(courseDto.getStatus())) {
            hasAccess = true;
        } else if (user != null) {
            if (courseDto.getInstructor() != null && user.getId().equals(courseDto.getInstructor().getId())) {
                hasAccess = true;
            } else if (user.getRole() == RoleType.MANAGER
                    || user.getRole() == RoleType.ADMIN) {
                hasAccess = true;
            } else {
                if (CourseStatus.DRAFT.equals(courseDto.getStatus())) {
                    hasAccess = false;
                } else {
                    Set<Integer> enrolledCourseIds = enrollmentService.getEnrolledCourseIds(user);
                    if (enrolledCourseIds.contains(id)) {
                        hasAccess = true;
                    }
                }
            }
        }

        if (!hasAccess) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khóa học không tồn tại hoặc chưa được xuất bản.");
            return "redirect:/courses";
        }

        model.addAttribute("course", courseDto);
        if (user != null) {
            Set<Integer> enrolledCourseIds = enrollmentService.getEnrolledCourseIds(user);
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);
            boolean hasReviewed = courseService.hasUserReviewedCourse(user.getId(), id);
            model.addAttribute("hasReviewed", hasReviewed);
            boolean canReview = courseService.canUserReviewCourse(user, id);
            model.addAttribute("canReview", canReview);
        }
        return "course/detail";
    }

    @PostMapping("/course/review/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCourseReview(@RequestParam("courseId") Integer courseId,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
        Map<String, Object> response = new HashMap<>();
        User user = getSessionUser();
        if (user == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập. Vui lòng đăng nhập để thực hiện chức năng này.");
            return ResponseEntity.status(401).body(response);
        }
        try {
            Map<String, Object> fbData = courseService.addCourseReviewAndBuildData(user, courseId, rating, comment);
            response.put("success", true);
            response.put("message", "Cảm ơn bạn đã gửi đánh giá khóa học thành công!");
            response.put("feedback", fbData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/course/review/edit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editCourseReview(@RequestParam("feedbackId") Integer feedbackId,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
        Map<String, Object> response = new HashMap<>();
        User user = getSessionUser();
        if (user == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập. Vui lòng đăng nhập để thực hiện chức năng này.");
            return ResponseEntity.status(401).body(response);
        }
        try {
            courseService.editCourseReview(feedbackId, rating, comment, user);
            response.put("success", true);
            response.put("message", "Cập nhật đánh giá thành công!");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/course/review/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCourseReview(@RequestParam("feedbackId") Integer feedbackId) {
        Map<String, Object> response = new HashMap<>();
        User user = getSessionUser();
        if (user == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập. Vui lòng đăng nhập để thực hiện chức năng này.");
            return ResponseEntity.status(401).body(response);
        }
        try {
            courseService.deleteCourseReview(feedbackId, user);
            response.put("success", true);
            response.put("message", "Xóa đánh giá thành công!");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

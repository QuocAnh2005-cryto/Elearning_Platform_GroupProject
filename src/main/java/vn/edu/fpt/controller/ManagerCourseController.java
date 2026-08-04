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
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.service.CategoryService;
import vn.edu.fpt.service.CourseService;
import vn.edu.fpt.service.ManagerCourseService;
import vn.edu.fpt.util.AppConstants;

import vn.edu.fpt.entity.SystemLog;
import vn.edu.fpt.repository.SystemLogRepository;
import java.util.List;

@Controller
@RequestMapping("/manager/course")
public class ManagerCourseController {

    private final ManagerCourseService managerCourseService;
    private final CourseService courseService;
    private final CategoryService categoryService;
    private final SystemLogRepository systemLogRepository;

    public ManagerCourseController(ManagerCourseService managerCourseService, 
                                   CourseService courseService, 
                                   CategoryService categoryService,
                                   SystemLogRepository systemLogRepository) {
        this.managerCourseService = managerCourseService;
        this.courseService = courseService;
        this.categoryService = categoryService;
        this.systemLogRepository = systemLogRepository;
    }

    /**
     * GET /manager/course/list
     * Hiển thị danh sách khóa học cần xét duyệt, hỗ trợ tìm kiếm và lọc theo trạng thái.
     */
    @GetMapping("/list")
    public String listCourses(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CourseDto> coursePage = managerCourseService.searchAndFilter(keyword, status, categoryId, pageable);

        int startPage = 0;
        int endPage = 0;
        if (coursePage.getTotalPages() > 0) {
            startPage = (coursePage.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
            endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, coursePage.getTotalPages() - 1);
        }

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("statuses", CourseStatus.values());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "manager/approval-course/course-list";
    }

    /**
     * GET /manager/course/detail/{id}
     * Hiển thị trang chi tiết của một khóa học.
     */
    @GetMapping("/detail/{id}")
    public String detailCourse(@PathVariable Integer id, Model model) {
        CourseDto course = courseService.getCourseDetail(id);
        List<SystemLog> courseLogs = systemLogRepository.findCourseLogs(String.valueOf(id));
        model.addAttribute("course", course);
        model.addAttribute("courseLogs", courseLogs);
        return "manager/approval-course/course-detail";
    }

    /**
     * POST /manager/course/edit/{id}
     * Cập nhật trạng thái khóa học (PHÊ DUYỆT, TỪ CHỐI, ẨN).
     * Exception (IllegalStateException, ObjectOptimisticLockingFailureException)
     * sẽ được GlobalExceptionHandler xử lý tập trung.
     */
    @PostMapping("/edit/{id}")
    public String updateCourseStatus(
            @PathVariable Integer id,
            @RequestParam("status") CourseStatus status,
            @RequestParam(value = "rejectionReason", required = false) String rejectionReason,
            RedirectAttributes redirectAttributes) {

        try {
            managerCourseService.updateCourseStatus(id, status, rejectionReason);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái khóa học thành công.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khóa học này đã được phê duyệt hoặc thay đổi bởi quản lý khác trước đó.");
        }
        return "redirect:/manager/course/detail/" + id;
    }
}

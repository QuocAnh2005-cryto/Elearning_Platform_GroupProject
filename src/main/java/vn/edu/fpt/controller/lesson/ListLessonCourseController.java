package vn.edu.fpt.controller.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.fpt.dto.course.CourseContentSidebarDTO;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.entity.*;
import vn.edu.fpt.service.*;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.dto.*;
import vn.edu.fpt.service.lesson.LessonProgressService;
import vn.edu.fpt.service.lesson.LessonService;
import vn.edu.fpt.service.section.CourseSectionService;
import vn.edu.fpt.util.SecurityUtils;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ListLessonCourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;
    private final EnrollmentService enrollmentService;
    private final CourseSectionService courseSectionService;
    private final FeedbackService feedbackService;
    private final DtoMapper dtoMapper;

    @Transactional
    @GetMapping("/course/{courseId}")
    public String listSection(@PathVariable Integer courseId) {
        User user = SecurityUtils.getCurrentUser();
        Course course = courseService.findByCourseIdAndUserId(courseId, user.getId());
        Integer lessonIdFinalCompleted = lessonService.findLessonIdFinalCompletedByCourseIdAndUserId(course.getId(), user.getId());
        Integer sectionId = lessonService.findSectionIdByLessonId(lessonIdFinalCompleted);
        return String.format("redirect:/course/%d/section/%d/lesson/%d", courseId, sectionId, lessonIdFinalCompleted);
    }

    @GetMapping("/course/{courseId}/section/{sectionId}/lesson/{lessonId}")
    public String viewLesson(Model model, @PathVariable Integer courseId, @PathVariable Integer sectionId, @PathVariable Integer lessonId) {
        User user = SecurityUtils.getCurrentUser();
        CourseContentSidebarDTO courseContentSidebarDTO = courseService.viewCourseContent(user, courseId, sectionId, lessonId);
        model.addAttribute("sidebar", courseContentSidebarDTO);
        return "learning/learning";
    }

    @GetMapping("/lesson/{lessonId}")
    @ResponseBody
    public String lessonView(@PathVariable("lessonId") Integer lessonId) {
        return lessonService.findLessonUrl(lessonId);
    }

    @GetMapping("/lesson-completed/{lessonId}")
    @ResponseBody
    public void updateLessonProgress(@PathVariable("lessonId") Integer lessonId) {
        User user = SecurityUtils.getCurrentUser();
        Integer sectionId = lessonService.findSectionIdByLessonId(lessonId);
        Integer courseId = courseSectionService.findCourseIdBySectionId(sectionId);
        Enrollment enrollment = enrollmentService.findEnrollmentByCourseIdAndUserId(courseId, user.getId());
        lessonProgressService.saveLessonProgressByEnrollmentAndLessonId(enrollment, lessonId);
        enrollmentService.updateEnrollmentProgressPercent(enrollment, courseId, sectionId);
    }


}

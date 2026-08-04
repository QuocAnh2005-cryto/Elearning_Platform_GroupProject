    package vn.edu.fpt.controller;

    import jakarta.persistence.criteria.CriteriaBuilder;
    import jakarta.validation.Valid;
    import org.springframework.data.domain.Page;
    import org.springframework.stereotype.Controller;
    import org.springframework.validation.BindingResult;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;
    import vn.edu.fpt.dto.course.CourseDto;
    import vn.edu.fpt.dto.CourseSectionDto;
    import vn.edu.fpt.dto.LessonDto;
    import vn.edu.fpt.dto.quizdto.QuizDTO;
    import vn.edu.fpt.entity.*;
    import vn.edu.fpt.service.cloud.VideoUploadService;
    import vn.edu.fpt.service.section.CourseSectionService;
    import vn.edu.fpt.service.CourseService;
    import vn.edu.fpt.service.material.LessonMaterialService;
    import vn.edu.fpt.service.lesson.LessonService;
    import vn.edu.fpt.service.quiz.QuizService;
    import vn.edu.fpt.util.SecurityUtils;

    import java.util.List;
    import java.util.Map;

    @Controller
    @RequestMapping("/instructor")
    public class InstructorLessonController {
        private final QuizService quizService;
        private final LessonService lessonService;
        private final CourseSectionService courseSectionService;
        private final CourseService courseService;
        private final LessonMaterialService lessonMaterialService;
        private final VideoUploadService videoUploadService;


        public InstructorLessonController(QuizService quizService, LessonService lessonService, CourseSectionService courseSectionService, CourseService courseService, LessonMaterialService lessonMaterialService, VideoUploadService videoUploadService) {
            this.quizService = quizService;
            this.lessonService = lessonService;
            this.courseSectionService = courseSectionService;
            this.courseService = courseService;
            this.lessonMaterialService = lessonMaterialService;
            this.videoUploadService = videoUploadService;
        }


        @PostMapping("/quiz/create-inline")
        public String createQuizInline(@ModelAttribute("quiz") QuizDTO quizDTO,
                                       @RequestParam("lessonId") Integer lessonId,
                                       @RequestParam("actionTarget") String actionTarget,
                                       RedirectAttributes attributes) {

            QuizDTO savedQuiz =
                    quizService.createQuiz(
                            lessonId,
                            quizDTO,
                            SecurityUtils.getCurrentUser());



            attributes.addFlashAttribute(
                    "success",
                    "Khởi tạo quiz thành công");

            if ("CONTINUE".equals(actionTarget)) {

                return "redirect:/instructor/quiz/quiz-manage/"
                        + savedQuiz.getId();
            }

            return "redirect:/instructor/lesson-detail/"
                    + lessonId;
        }

        @GetMapping("/lesson-detail/{id}")
        public String getLessonDetail(@PathVariable("id") Integer lessonId,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                      @RequestParam(value = "status", defaultValue = "ALL") String status,
                                      Model model) {
            User currentUser = SecurityUtils.getCurrentUser();

            model.addAttribute("currentUser", currentUser);
            Lesson lesson = lessonService.findLessonById(lessonId);
            courseService.getInstructorOwnedCourse(
                    lesson.getCourseSection().getCourse().getId(),
                    currentUser);
            LessonDto lessonDto = lessonService.getLessonById(lessonId);
            CourseSectionDto courseSectionDto = courseSectionService.findByCourseSectionId(lesson.getCourseSection().getId());
            CourseDto courseDto = courseService.getCourseDetail(lesson.getCourseSection().getCourse().getId());

            Page<QuizDTO> quizPage = quizService.getQuizzesByStatus(page, size, status, lessonId);


            long endItem = Math.min(
                    (long) (quizPage.getNumber() + 1) * quizPage.getSize(),
                    quizPage.getTotalElements()
            );
            model.addAttribute("endItem", endItem);
            model.addAttribute("quiz", new QuizDTO());
            model.addAttribute("quizPage", quizPage);                      // Đối tượng Page để render thanh chuyển trang
            model.addAttribute("quizzes", quizPage.getContent());           // Danh sách Quiz của trang hiện tại
            model.addAttribute("currentStatus", status);                    // Để giữ trạng thái Active cho nút bấm Bộ lọc

            model.addAttribute("lessonMaterials", lesson.getMaterials());
            model.addAttribute("lesson", lessonDto);
            model.addAttribute("course", courseDto);
            model.addAttribute("section", courseSectionDto);
            return "instructor_course/lesson-detail";
        }

        @GetMapping("/lesson-detail/{id}/quizzes-fragment")
        public String getQuizzesFragment(@PathVariable("id") Integer lessonId,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                         @RequestParam(value = "status", defaultValue = "ALL") String status,
                                         Model model) {

            User currentUser = SecurityUtils.getCurrentUser();
            Lesson lesson = lessonService.findLessonById(lessonId);
            courseService.getInstructorOwnedCourse(
                    lesson.getCourseSection().getCourse().getId(),
                    currentUser);
            Page<QuizDTO> quizPage = quizService.getQuizzesByStatus(page, size, status, lessonId);

            long endItem = Math.min(
                    (long) (quizPage.getNumber() + 1) * quizPage.getSize(),
                    quizPage.getTotalElements()
            );

            model.addAttribute("endItem", endItem);
            model.addAttribute("quizPage", quizPage);
            model.addAttribute("quizzes", quizPage.getContent());
            model.addAttribute("currentStatus", status);

        LessonDto lessonDto = lessonService.getLessonById(lessonId);
        model.addAttribute("lesson", lessonDto);

            model.addAttribute("quiz", new QuizDTO());

            return "instructor_course/lesson-detail :: quizListSection";
        }


        @PostMapping("/sections/{sectionId}/lessons")
        public String createLesson(
                @RequestParam(value = "source", defaultValue = "create") String source,
                @PathVariable("sectionId") Integer sectionId,
                @RequestParam("courseId") Integer courseId,
                @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                @RequestParam(value = "videoBlobName", required = false) String videoBlobName,
                @RequestParam(value = "materialFiles", required = false) List<MultipartFile> materials,
                @Valid @ModelAttribute("lesson") LessonDto lessonDto,
                BindingResult bindingResult,
                RedirectAttributes redirectAttributes) {

            User instructor = SecurityUtils.getCurrentUser();

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("error", "Dữ liệu nhập không hợp lệ.");
                return redirectAfterCurriculumAction(source, courseId);
            }

            try {
                Lesson tmp = lessonService.saveLesson(sectionId, lessonDto, videoFile, videoBlobName);
                lessonMaterialService.saveAllMaterial(materials, tmp.getId(), instructor);
                redirectAttributes.addFlashAttribute("success", "Thêm bài giảng thành công!");
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            }

            return redirectAfterCurriculumAction(source, courseId);
        }

        @PostMapping("/sections/{sectionId}/lessons/{lessonId}/edit")
        public String updateLesson(
                @RequestParam(value = "source", defaultValue = "create") String source,
                @PathVariable("sectionId") Integer sectionId,
                @PathVariable("lessonId") Integer lessonId,
                @RequestParam("courseId") Integer courseId,
                @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                @RequestParam(value = "videoBlobName", required = false) String videoBlobName,
                @ModelAttribute("lesson") LessonDto lessonDto,
                RedirectAttributes redirectAttributes) {

            User instructor = SecurityUtils.getCurrentUser();

            try {
                lessonService.updateLesson(lessonId, lessonDto, videoFile, videoBlobName);
                redirectAttributes.addFlashAttribute("success", "Cập nhật bài giảng thành công!");
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            }

            return redirectAfterCurriculumAction(source, courseId);
        }

        @PostMapping("/video-upload-url")
        @ResponseBody
        public Map<String, String> uploadVideo(@RequestParam("fileName") String fileName,
                                               @RequestParam("sectionId") Integer sectionId){
            User user = SecurityUtils.getCurrentUser();
            return videoUploadService.generateDirectUploadUrl(fileName, sectionId, user);

        }
        @PostMapping("/lessons/{lessonId}/delete")
        public String deleteLesson(
                @RequestParam(value = "source", defaultValue = "create") String source,
                @PathVariable("lessonId") Integer lessonId,
                @RequestParam("courseId") Integer courseId,
                RedirectAttributes redirectAttributes) {

            try {
                User user = SecurityUtils.getCurrentUser();
                lessonService.deleteLesson(lessonId, user);
                redirectAttributes.addFlashAttribute("success", "Xóa bài giảng thành công!");
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            }

            return redirectAfterCurriculumAction(source, courseId);
        }

        private String redirectAfterCurriculumAction(String source, Integer courseId) {
            return "edit".equals(source)
                    ? "redirect:/instructor/" + courseId + "/edit"
                    : "redirect:/instructor/" + courseId + "/curriculum";
        }



}


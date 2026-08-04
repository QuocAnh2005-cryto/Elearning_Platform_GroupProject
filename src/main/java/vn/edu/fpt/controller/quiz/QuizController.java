package vn.edu.fpt.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.dto.quizdto.LessonQuizPageDTO;
import vn.edu.fpt.dto.quizdto.QuizSubmitDTO;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.QuizAttempt;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.lesson.LessonService;
import vn.edu.fpt.service.quiz.QuizAttemptService;
import vn.edu.fpt.service.quiz.QuizService;
import vn.edu.fpt.util.SecurityUtils;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizAttemptService quizAttemptService;
    private final LessonService lessonService;

    @GetMapping("/quiz/lesson/{lessonId}")
    public String viewQuiz(@PathVariable("lessonId") Integer lessonId, @RequestParam(value = "retake", required = false, defaultValue = "false") boolean retake, @RequestParam(value = "activeQuizId", required = false) Integer activeQuizId, @RequestParam(value = "activeAttemptId", required = false) Integer activeAttemptId, Model model) {
        User user = SecurityUtils.getCurrentUser();
        Lesson lesson = lessonService.findById(lessonId).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy bài học"));
        if (!lessonService.hasAccessToLesson(user, lesson)) {
            throw new AccessDeniedException("Bạn không có quyền truy cập bài học này");
        }
        LessonQuizPageDTO page = quizService.getLessonQuizPage(lessonId, user, retake, activeQuizId, activeAttemptId);
        model.addAttribute("page", page);
        return "quiz/lesson-quiz";
    }

    @GetMapping("/instructor/quiz/lesson-preview/{lessonId}")
    public String previewLessonQuiz(@PathVariable("lessonId") Integer lessonId,
                                    @RequestParam(value = "activeQuizId", required = false) Integer activeQuizId,
                                    Model model) {
        User user = SecurityUtils.getCurrentUser();
        Lesson lesson = lessonService.findById(lessonId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy bài học"));
        Integer ownerId = lesson.getCourseSection().getCourse().getInstructor().getId();
        if (!java.util.Objects.equals(ownerId, user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xem trước quiz của bài học này");
        }
        // Instructor viewCourse preview: chi xem noi dung quiz, khong tao lich su lam bai.
        LessonQuizPageDTO page = quizService.getLessonQuizPreviewPage(lessonId, activeQuizId);
        model.addAttribute("page", page);
        return "quiz/lesson-quiz";
    }

    @PostMapping("/quiz/submit/{quizId}")
    public String submitQuiz(@PathVariable("quizId") Integer quizId, @ModelAttribute QuizSubmitDTO submitDTO) {
        User user = SecurityUtils.getCurrentUser();
        vn.edu.fpt.entity.Quiz quiz = quizService.findById(quizId).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy bài tập"));
        if (!lessonService.hasAccessToLesson(user, quiz.getLesson())) {
            throw new AccessDeniedException("Bạn không có quyền làm bài tập này");
        }
        if (!"PUBLISHED".equalsIgnoreCase(quiz.getStatus())) {
            throw new AccessDeniedException("Bài tập này chưa được xuất bản.");
        }
        submitDTO.setQuizId(quizId);
        QuizAttempt attempt = quizAttemptService.submitQuiz(quizId, user, submitDTO);
        return "redirect:/quiz/lesson/" + attempt.getQuiz().getLesson().getId() + "?activeQuizId=" + quizId;
    }
}

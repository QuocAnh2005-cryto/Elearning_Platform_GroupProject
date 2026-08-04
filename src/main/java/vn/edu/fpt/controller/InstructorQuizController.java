package vn.edu.fpt.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import vn.edu.fpt.dto.UserDto;
import vn.edu.fpt.dto.quizdto.QuizAttemptDTO;
import vn.edu.fpt.dto.quizdto.QuizDTO;
import vn.edu.fpt.dto.quizdto.QuizQuestionDTO;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.QuestionType;
import vn.edu.fpt.enums.QuizStatus;
import vn.edu.fpt.service.quiz.QuizAttemptService;
import vn.edu.fpt.service.quiz.QuizImportService;
import vn.edu.fpt.service.quiz.QuizQuestionService;
import vn.edu.fpt.service.quiz.QuizService;
import vn.edu.fpt.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/instructor/quiz")
public class InstructorQuizController {

    private final QuizService quizService;
    private final QuizQuestionService quizQuestionService;
    private final QuizAttemptService quizAttemptService;
    private final QuizImportService quizImportService;


    public InstructorQuizController(QuizService quizService, QuizQuestionService quizQuestionService, QuizAttemptService quizAttemptService, QuizImportService quizImportService)
    {
        this.quizService = quizService;
        this.quizQuestionService = quizQuestionService;
        this.quizAttemptService = quizAttemptService;
        this.quizImportService = quizImportService;
    }


    @GetMapping("/add-question/{quizId}")
    public String CreateQuestionForm(
            @PathVariable("quizId") Integer quizId,
            Model model) {

        User currentUser = SecurityUtils.getCurrentUser();
        model.addAttribute("currentUser", currentUser);


        QuizDTO quizDto = quizService.findQuizById(quizId, currentUser);
        model.addAttribute("quiz", quizDto);
        model.addAttribute("question", new QuizQuestionDTO());
        model.addAttribute("questionTypes", QuestionType.values());



        return "instructor_course/question-create";
    }

    @PostMapping("/save-question")
    public String saveQuestion(
            @ModelAttribute("question") QuizQuestionDTO questionDTO,
            @RequestParam("quizId") Integer quizId,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "3") Integer size,
            RedirectAttributes redirectAttributes
            ) {

        User currentUser = SecurityUtils.getCurrentUser();
        quizQuestionService.saveQuestion(questionDTO, quizId, currentUser);
        System.out.println("-----START-----");
        System.out.println(questionDTO.getExplanation());
        System.out.println("-----END-----");
        String message = "";
        // EDIT
        if(questionDTO.getId() != null){
            message = "Cập nhật câu hỏi thành công!";
            redirectAttributes.addFlashAttribute("toastMessage", message);
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/instructor/quiz/quiz-manage/"
                    + quizId
                    + "?page=" + page
                    + "&size=" + size;

        }

        // CREATE
        message = "Thêm mới câu hỏi thành công!";
        long totalQuestions =
                quizQuestionService.getTotalQuestionsByQuizId(quizId);

        int targetPage =
                (int)((totalQuestions - 1) / size);

        redirectAttributes.addFlashAttribute("toastMessage", message);
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/instructor/quiz/quiz-manage/"
                + quizId
                + "?page=" + targetPage
                + "&size=" + size;
    }

    @GetMapping("/copy-question/{questionId}")
    public String copyQuestion(
            @PathVariable Integer questionId,
            @RequestParam Integer quizId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            RedirectAttributes redirectAttributes
    ) {

        User currentUser = SecurityUtils.getCurrentUser();
        Integer newPosition =
                quizQuestionService.copyQuestion(questionId, quizId, currentUser);

        int targetPage =
                (newPosition - 1) / size;

        redirectAttributes.addFlashAttribute(
                "toastMessage",
                "sao chép câu hỏi thành công"
        );

        redirectAttributes.addFlashAttribute(
                "toastType",
                "success"
        );

        return "redirect:/instructor/quiz/quiz-manage/"
                + quizId
                + "?page=" + targetPage
                + "&size=" + size;
    }

    @GetMapping("delete-question/{questionId}")
    String deteleQuestion(@PathVariable("questionId") Integer questionId,
                          @RequestParam("quizId") Integer quizId,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "3") int size,
                          RedirectAttributes redirectAttributes,
                          Model model){
        User currentUser = SecurityUtils.getCurrentUser();
        quizQuestionService.deleteQuestion(questionId, quizId, currentUser);
        int totalQuestions =
                quizQuestionService.getTotalQuestionsByQuizId(quizId);

        int totalPages =
                (int) Math.ceil((double) totalQuestions / size);

        if (page >= totalPages && page > 0) {
            page--;
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Đã xóa câu hỏi khỏi bộ trắc nghiệm.");
        redirectAttributes.addFlashAttribute("toastType", "success");

        return "redirect:/instructor/quiz/quiz-manage/"
                + quizId
                + "?page=" + page
                + "&size=" + size;
    }

    @GetMapping("edit-question/{questionId}")
    String editQuestion(@PathVariable("questionId") Integer questionId,
                        @RequestParam("quizId") Integer quizId,
                        @RequestParam int page,
                        @RequestParam int size,
                        Model model){

        User currentUser = SecurityUtils.getCurrentUser();
        QuizDTO quizDto = quizService.findQuizById(quizId, currentUser);
        QuizQuestionDTO quizQuestionDto = quizQuestionService.findQuizQuestionById(questionId, quizId, currentUser);

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("quiz", quizDto);
        model.addAttribute("question", quizQuestionDto);
        model.addAttribute("questionTypes", QuestionType.values());

        return "instructor_course/question-create";
    }



    @GetMapping("/quiz-manage/{quizId}")
    String quizManagePage(@PathVariable("quizId") Integer quizId,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "3") int size,
                          Model model){
        User currentUser = SecurityUtils.getCurrentUser();
        QuizDTO quizDto = quizService.findQuizById(quizId, currentUser);

        Page<QuizQuestionDTO> questionPage = quizQuestionService.getQuestionsByQuizId(quizId, page, size, currentUser);

        // 2. Đẩy các thuộc tính phân trang ra Model khớp với các biến trong HTML
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("quiz", quizDto);
        model.addAttribute("questions", questionPage.getContent()); // danh sách câu hỏi trang hiện tại
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", questionPage.getTotalPages());

        return "instructor_course/manage-questions";
    }

    @GetMapping("save-status/{quizId}")
    String saveStatus(@PathVariable("quizId") Integer quizId,
                      @RequestParam("status") String status,
                      RedirectAttributes redirectAttributes){

        User currentUser = SecurityUtils.getCurrentUser();
        QuizDTO quizDto = quizService.findQuizById(quizId, currentUser);
        String currentStatus = "";

        if(QuizStatus.DRAFT.name().equals(status)){
            quizService.saveDraft(quizId, currentUser);
            currentStatus = QuizStatus.DRAFT.name();
            redirectAttributes.addFlashAttribute("toastMessage", "Đã lưu bản nháp bài trắc nghiệm thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        }
        else if(QuizStatus.PUBLISHED.name().equals(status)){
            if(quizService.publishQuiz(quizId, currentUser)){
                currentStatus = QuizStatus.PUBLISHED.name();
                redirectAttributes.addFlashAttribute("toastMessage", "Bài trắc nghiệm đã được xuất bản công khai.");
                redirectAttributes.addFlashAttribute("toastType", "success");
            }
            else{
                redirectAttributes.addFlashAttribute("publishError", "Không thể xuất bản! Bài trắc nghiệm này hiện đang trống. Vui lòng thêm ít nhất 1 câu hỏi.");
                return "redirect:/instructor/quiz/quiz-manage/" + quizId;
            }


        }
        long totalQuizzesWithStatus = quizService.getTotalQuizByLessonIdAndStatus(quizDto.getLessonId(), currentStatus);

        int pageSize = 5;
        int targetPage = (int)((totalQuizzesWithStatus - 1) / pageSize);
        if (targetPage < 0) targetPage = 0;


        return "redirect:/instructor/lesson-detail/"
                + quizDto.getLessonId()
                + "?page=" + targetPage
                + "&size=" + pageSize
                + "&status=" + currentStatus;
    }

    @PostMapping("/update-quiz-meta")
    public String updateQuizMeta(
            @ModelAttribute("quiz") QuizDTO quizDTO) {

        quizService.updateQuizMeta(quizDTO, SecurityUtils.getCurrentUser());

        return "redirect:/instructor/quiz/quiz-manage/" + quizDTO.getId();
    }

    @PostMapping("/{quizId}/delete") // Đổi sang PostMapping
    public ResponseEntity<?> deleteQuiz(@PathVariable("quizId") Integer quizId) {
        try {
            quizService.deleteQuiz(quizId, SecurityUtils.getCurrentUser());
            return ResponseEntity.ok(Map.of("success", true, "message", "Xóa bài trắc nghiệm thành công!"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Lỗi: Không thể xóa bài trắc nghiệm này."));
        }
    }

    @PostMapping("/{quizId}/archive")
    public ResponseEntity<?> archiveQuiz(@PathVariable("quizId") Integer quizId) {
        try {
            quizService.archived(quizId, SecurityUtils.getCurrentUser()); // Logic chuyển status thành 'ARCHIVED'
            return ResponseEntity.ok(Map.of("success", true, "message", "Đã đưa bài trắc nghiệm vào kho lưu trữ!"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Lỗi: Không thể lưu trữ bài trắc nghiệm này."));
        }
    }

    @GetMapping("/view-detail/{quizId}")
    public String viewQuizDetail(
            @PathVariable Integer quizId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size, // Để dễ test phân trang nên để size nhỏ
            @RequestParam(defaultValue = "questions") String tab,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(defaultValue = "submittedAtDesc") String sortBy,
            @RequestParam(required = false) String status,
            Model model) {

        // 1. Giả lập thông tin Giảng viên hiện tại
        User currentUser = SecurityUtils.getCurrentUser();
        model.addAttribute("currentUser", currentUser);

        // 2. Giả lập thông tin bộ Quiz tổng quan
        QuizDTO quizDto = quizService.findQuizById(quizId, currentUser);
        model.addAttribute("quiz", quizDto);
        Integer currentSize = null;
        // 3. Xử lý logic Mock Data theo từng Tab để test phân trang chuẩn Spring Data
        if ("attempts".equals(tab)) {
            currentSize = (size == 1) ? 10 : size;
            Page<QuizAttemptDTO> attemptsPage = quizAttemptService.getAllAttemptByQuizId(quizId, page, currentSize, sortBy, searchKeyword, status);

            // Tính toán phân trang thủ công trên List để giả lập Page của Spring Data

            model.addAttribute("attempts", attemptsPage.getContent());
            model.addAttribute("totalPages", attemptsPage.getTotalPages());
            model.addAttribute("searchKeyword", searchKeyword);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("status", status);
        } else {
            // Tab 'questions'
            currentSize = (size == 1) ? 3 : size;
            Page<QuizQuestionDTO> quizQuestionsPage = quizQuestionService.getQuestionsByQuizId(quizId, page, currentSize, currentUser);

            model.addAttribute("questions", quizQuestionsPage.getContent());
            model.addAttribute("totalPages", quizQuestionsPage.getTotalPages());
        }

        // Gửi các biến cấu hình phân trang & tab hoạt động về giao diện
        model.addAttribute("activeTab", tab);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", currentSize);

        return "instructor_course/quiz-detail"; // Tên file HTML của bạn (view-quiz-detail.html)
    }

    @PostMapping("/import-excel")
    public String importExcel(

            @RequestParam("excelFile")
            MultipartFile excelFile,

            @RequestParam("quizId")
            Integer quizId,

            @RequestParam("importMode")
            String importMode,

            RedirectAttributes redirectAttributes

    ) {

        try {

            quizImportService.importQuiz(

                    excelFile,

                    quizId,

                    importMode,

                    SecurityUtils.getCurrentUser()

            );

            redirectAttributes.addFlashAttribute("toastMessage", "Import quiz thành công !");
            redirectAttributes.addFlashAttribute("toastType", "success");

        }

        catch (Exception ex) {
            log.error("Error importing quiz from Excel: ", ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");

        }

        return "redirect:/instructor/quiz/quiz-manage/" + quizId;

    }

}

package vn.edu.fpt.service.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.quizdto.LessonQuizPageDTO;
import vn.edu.fpt.dto.quizdto.QuizAnswerViewDTO;
import vn.edu.fpt.dto.quizdto.QuizAttemptViewDTO;
import vn.edu.fpt.dto.quizdto.QuizDTO;
import vn.edu.fpt.dto.quizdto.QuizQuestionDTO;
import vn.edu.fpt.dto.quizdto.QuizQuestionViewDTO;
import vn.edu.fpt.dto.quizdto.QuizViewDTO;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.Quiz;
import vn.edu.fpt.entity.QuizAnswer;
import vn.edu.fpt.entity.QuizAttempt;
import vn.edu.fpt.entity.QuizAttemptAnswer;
import vn.edu.fpt.entity.QuizQuestion;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.QuizStatus;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.LessonRepository;
import vn.edu.fpt.repository.QuizAttemptAnswerRepository;
import vn.edu.fpt.repository.QuizAttemptRepository;
import vn.edu.fpt.repository.QuizQuestionRepository;
import vn.edu.fpt.repository.QuizRepository;
import vn.edu.fpt.service.CourseService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class QuizService {
    private final QuizRepository repository;
    private final DtoMapper dtoMapper;
    private final LessonRepository lessonRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizAttemptAnswerRepository quizAttemptAnswerRepository;
    private final CourseService courseService;

    public QuizService(QuizRepository quizRepository, DtoMapper dtoMapper, LessonRepository lessonRepository, QuizQuestionRepository quizQuestionRepository, QuizAttemptRepository quizAttemptRepository, QuizAttemptAnswerRepository quizAttemptAnswerRepository, CourseService courseService) {
        this.repository = quizRepository;
        this.dtoMapper = dtoMapper;
        this.lessonRepository = lessonRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizAttemptAnswerRepository = quizAttemptAnswerRepository;
        this.courseService = courseService;
    }

    public LessonQuizPageDTO getLessonQuizPage(Integer lessonId, User user, boolean retake, Integer activeQuizId, Integer activeAttemptId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lesson với id " + lessonId));

        List<Quiz> quizzes = repository.findByLessonId(lessonId);
        List<Quiz> publishedQuizzes = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            if ("PUBLISHED".equalsIgnoreCase(quiz.getStatus())) {
                publishedQuizzes.add(quiz);
            }
        }
        quizzes = publishedQuizzes;
        sortQuizzes(quizzes);

        Integer selectedQuizId = chooseActiveQuizId(quizzes, activeQuizId);
        List<QuizViewDTO> quizViews = new ArrayList<>();
        QuizViewDTO activeQuiz = null;

        for (Quiz quiz : quizzes) {
            boolean active = Objects.equals(quiz.getId(), selectedQuizId);
            List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdAndQuizIdOrderBySubmittedAtDesc(user.getId(), quiz.getId());
            QuizAttempt selectedAttempt = active ? chooseSelectedAttempt(attempts, activeAttemptId) : null;
            boolean showingResult = active && !retake && selectedAttempt != null;
            Set<Integer> selectedAnswerIds = showingResult ? findSelectedAnswerIds(selectedAttempt.getId()) : Set.of();
            QuizViewDTO quizView = toQuizView(quiz, attempts, selectedAttempt, selectedAnswerIds, active, showingResult);

            quizViews.add(quizView);
            if (active) {
                activeQuiz = quizView;
            }
        }

        return LessonQuizPageDTO.builder()
                .lessonId(lesson.getId())
                .lessonTitle(lesson.getTitle())
                .activeQuizId(selectedQuizId)
                .retake(retake)
                .activeQuiz(activeQuiz)
                .quizzes(quizViews)
                .build();
    }

    public LessonQuizPageDTO getLessonQuizPreviewPage(Integer lessonId, Integer activeQuizId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lesson với id " + lessonId));

        List<Quiz> quizzes = repository.findByLessonId(lessonId);
        List<Quiz> publishedQuizzes = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            if ("PUBLISHED".equalsIgnoreCase(quiz.getStatus())) {
                publishedQuizzes.add(quiz);
            }
        }
        sortQuizzes(publishedQuizzes);

        Integer selectedQuizId = chooseActiveQuizId(publishedQuizzes, activeQuizId);
        List<QuizViewDTO> quizViews = new ArrayList<>();
        QuizViewDTO activeQuiz = null;

        for (Quiz quiz : publishedQuizzes) {
            boolean active = Objects.equals(quiz.getId(), selectedQuizId);
            // Instructor preview: chi render noi dung quiz, khong doc/ghi lich su lam bai.
            QuizViewDTO quizView = toQuizView(quiz, List.of(), null, Set.of(), active, false);
            quizViews.add(quizView);
            if (active) {
                activeQuiz = quizView;
            }
        }

        return LessonQuizPageDTO.builder()
                .lessonId(lesson.getId())
                .lessonTitle(lesson.getTitle())
                .activeQuizId(selectedQuizId)
                .previewMode(true)
                .activeQuiz(activeQuiz)
                .quizzes(quizViews)
                .build();
    }

    private void sortQuizzes(List<Quiz> quizzes) {
        quizzes.sort((q1, q2) -> {
            Integer id1 = q1.getId();
            Integer id2 = q2.getId();
            if (id1 == null && id2 == null) return 0;
            if (id1 == null) return 1;
            if (id2 == null) return -1;
            return id1.compareTo(id2);
        });
    }

    private Integer chooseActiveQuizId(List<Quiz> quizzes, Integer activeQuizId) {
        if (quizzes == null || quizzes.isEmpty()) {
            return null;
        }

        if (activeQuizId == null) {
            return quizzes.get(0).getId();
        }

        for (Quiz quiz : quizzes) {
            if (Objects.equals(quiz.getId(), activeQuizId)) {
                return activeQuizId;
            }
        }

        return quizzes.get(0).getId();
    }

    private QuizAttempt chooseSelectedAttempt(List<QuizAttempt> attempts, Integer activeAttemptId) {
        if (attempts == null || attempts.isEmpty()) {
            return null;
        }

        if (activeAttemptId == null) {
            return attempts.get(0);
        }

        for (QuizAttempt attempt : attempts) {
            if (Objects.equals(attempt.getId(), activeAttemptId)) {
                return attempt;
            }
        }

        return attempts.get(0);
    }

    private Set<Integer> findSelectedAnswerIds(Integer attemptId) {
        List<QuizAttemptAnswer> attemptAnswers = quizAttemptAnswerRepository.findByAttemptId(attemptId);
        Set<Integer> selectedAnswerIds = new HashSet<>();

        for (QuizAttemptAnswer attemptAnswer : attemptAnswers) {
            if (attemptAnswer.getSelectedAnswer() != null) {
                selectedAnswerIds.add(attemptAnswer.getSelectedAnswer().getId());
            }
        }

        return selectedAnswerIds;
    }

    private QuizViewDTO toQuizView(Quiz quiz, List<QuizAttempt> attempts, QuizAttempt selectedAttempt, Set<Integer> selectedAnswerIds, boolean active, boolean showingResult) {
        return QuizViewDTO.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .passScorePercent(quiz.getPassScorePercent())
                .timeLimitMinutes(quiz.getTimeLimitMinutes())
                .active(active)
                .showingResult(showingResult)
                .selectedAttempt(toAttemptView(selectedAttempt, attempts, true))
                .attempts(toAttemptViews(attempts, selectedAttempt))
                .questions(toQuestionViews(
                        quiz.getQuestions(),
                        selectedAnswerIds,
                        Boolean.TRUE.equals(quiz.getIsRandomQuestion()) && !showingResult
                ))
                .build();
    }

    private List<QuizAttemptViewDTO> toAttemptViews(List<QuizAttempt> attempts, QuizAttempt selectedAttempt) {
        List<QuizAttemptViewDTO> result = new ArrayList<>();
        if (attempts == null) {
            return result;
        }

        int totalAttempts = attempts.size();
        for (int index = 0; index < attempts.size(); index++) {
            QuizAttempt attempt = attempts.get(index);
            boolean active = selectedAttempt != null && Objects.equals(attempt.getId(), selectedAttempt.getId());
            result.add(toAttemptView(attempt, totalAttempts - index, index == 0, active));
        }

        return result;
    }

    private QuizAttemptViewDTO toAttemptView(QuizAttempt attempt, List<QuizAttempt> attempts, boolean active) {
        if (attempt == null) {
            return null;
        }

        int attemptIndex = attempts != null ? attempts.indexOf(attempt) : -1;
        int attemptNumber = attemptIndex >= 0 ? attempts.size() - attemptIndex : 1;
        boolean latest = attemptIndex == 0;

        return toAttemptView(attempt, attemptNumber, latest, active);
    }

    private QuizAttemptViewDTO toAttemptView(QuizAttempt attempt, int attemptNumber, boolean latest, boolean active) {
        return QuizAttemptViewDTO.builder()
                .id(attempt.getId())
                .score(attempt.getScore())
                .passed(Boolean.TRUE.equals(attempt.getPassed()))
                .submittedAt(attempt.getSubmittedAt())
                .attemptNumber(attemptNumber)
                .latest(latest)
                .active(active)
                .build();
    }

    private List<QuizQuestionViewDTO> toQuestionViews(List<QuizQuestion> questions, Set<Integer> selectedAnswerIds, boolean randomQuestion) {
        List<QuizQuestion> sortedQuestions = new ArrayList<>(questions != null ? questions : List.of());
        sortedQuestions.sort(Comparator
                .comparing(QuizQuestion::getPosition, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(QuizQuestion::getId, Comparator.nullsLast(Integer::compareTo)));

        if (randomQuestion) {
            Collections.shuffle(sortedQuestions);
        }

        List<QuizQuestionViewDTO> result = new ArrayList<>();
        for (int index = 0; index < sortedQuestions.size(); index++) {
            QuizQuestion question = sortedQuestions.get(index);
            result.add(QuizQuestionViewDTO.builder()
                    .id(question.getId())
                    .questionText(question.getQuestionText())
                    .questionType(question.getQuestionType())
                    .points(question.getPoints())
                    .position(question.getPosition())
                    .explanation(question.getExplanation())
                    .formIndex(index)
                    .answers(toAnswerViews(question.getAnswers(), selectedAnswerIds))
                    .build());
        }

        return result;
    }

    private List<QuizAnswerViewDTO> toAnswerViews(List<QuizAnswer> answers, Set<Integer> selectedAnswerIds) {
        List<QuizAnswer> sortedAnswers = new ArrayList<>(answers != null ? answers : List.of());
        sortedAnswers.sort(Comparator
                .comparing(QuizAnswer::getPosition, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(QuizAnswer::getId, Comparator.nullsLast(Integer::compareTo)));

        List<QuizAnswerViewDTO> result = new ArrayList<>();
        for (QuizAnswer answer : sortedAnswers) {
            result.add(QuizAnswerViewDTO.builder()
                    .id(answer.getId())
                    .answerText(answer.getAnswerText())
                    .position(answer.getPosition())
                    .correct(Boolean.TRUE.equals(answer.getCorrect()))
                    .selected(selectedAnswerIds != null && selectedAnswerIds.contains(answer.getId()))
                    .build());
        }

        return result;
    }

    public List<Quiz> findAll() {
        return repository.findAll();
    }

    public Optional<Quiz> findById(Integer id) {
        return repository.findById(id);
    }

    public Quiz save(Quiz entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public int totalQuestion(List<QuizDTO> quizzes) {
        int total = 0;
        for (QuizDTO quizDTO : quizzes) {
            if (quizDTO.getQuestions() == null || quizDTO.getQuestions().isEmpty()) {
                continue;
            }
            total += quizDTO.getQuestions().size();
        }
        return total;
    }

    public QuizDTO createQuiz(
            Integer lessonId,
            QuizDTO request,
            User instructor) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lesson not found"));
        courseService.getInstructorOwnedCourse(
                lesson.getCourseSection().getCourse().getId(),
                instructor);

        Quiz quiz = Quiz.builder()
                .lesson(lesson)
                .title(request.getTitle())
                .description(request.getDescription())
                .passScorePercent(request.getPassScorePercent())
                .timeLimitMinutes(request.getTimeLimitMinutes())
                .status(QuizStatus.DRAFT.name())
                .isRandomQuestion(
                        Boolean.TRUE.equals(
                                request.getIsRandomQuestion()))
                .isRandomAnswer(
                        Boolean.TRUE.equals(
                                request.getIsRandomAnswer()))
                .build();

        Quiz savedQuiz = repository.save(quiz);

        return dtoMapper.toQuizDto(savedQuiz);
    }

    public List<QuizDTO> getQuizzesByLesson(Integer lessonId) {

        List<Quiz> quizzes =
                repository.findByLessonId((lessonId));

        List<QuizDTO> result = new ArrayList<>();

        for (Quiz quiz : quizzes) {

            QuizDTO dto =
                    dtoMapper.toQuizDto(quiz);

            result.add(dto);
        }

        return result;
    }

    public Quiz getInstructorOwnedQuiz(Integer quizId, User instructor) {
        Quiz quiz = repository.findQuizById(quizId);
        if (quiz == null) {
            throw new ResourceNotFoundException("Không tìm thấy quiz với id " + quizId);
        }

        courseService.getInstructorOwnedCourse(
                quiz.getLesson().getCourseSection().getCourse().getId(),
                instructor);
        return quiz;
    }

    public QuizDTO findQuizById(Integer quizId, User instructor){
        return dtoMapper.toQuizDto(getInstructorOwnedQuiz(quizId, instructor));
    }

    public Page<QuizDTO> getQuizzesByStatus(int page, int size, String status, Integer lessonId){
        Pageable pageable = PageRequest.of(page, size);

        Page<Quiz> quizPage = repository.findByLessonIdAndStatus(lessonId,status, pageable);

        List<QuizDTO> dtos = new ArrayList<>();
        for (Quiz dto : quizPage.getContent()){
            dtos.add(dtoMapper.toQuizDto(dto));
        }

        return new PageImpl<>(
                dtos,
                quizPage.getPageable(),
                quizPage.getTotalElements()
        );

    }

    public void updateQuizMeta(QuizDTO quizDto, User instructor){
        Quiz quiz = getInstructorOwnedQuiz(quizDto.getId(), instructor);

        quiz.setTitle(quizDto.getTitle());
        quiz.setPassScorePercent(quizDto.getPassScorePercent());
        quiz.setTimeLimitMinutes(quizDto.getTimeLimitMinutes());


    }

    public void archived(Integer quizId, User instructor){
        Quiz quiz = getInstructorOwnedQuiz(quizId, instructor);

        quiz.setStatus(QuizStatus.ARCHIVED.name());
    }

    public void saveDraft(Integer quizId, User instructor){
        Quiz quiz = getInstructorOwnedQuiz(quizId, instructor);

        quiz.setStatus(QuizStatus.DRAFT.name());
    }

    public boolean publishQuiz(Integer quizId, User instructor){
        Quiz quiz = getInstructorOwnedQuiz(quizId, instructor);

    long quizQuestionCount = quizQuestionRepository.countByQuizId(quizId);
        if(quizQuestionCount < 1){
            System.out.println("Quiz need at least 1 question to publish!");
            return false;
        }

        quiz.setStatus(QuizStatus.PUBLISHED.name());
        quiz.setPublishedAt(LocalDateTime.now());
        return true;
    }

    public void deleteQuiz(Integer quizId, User instructor) {
        Quiz quiz = getInstructorOwnedQuiz(quizId, instructor);

        repository.delete(quiz);
    }

    public Long getTotalQuizByLessonId(Integer lessonId){
        return repository.countByLessonId(lessonId);
    }

    public Long getTotalQuizByLessonIdAndStatus(Integer lessonId,String status){
        return repository.countByLessonIdAndStatus(lessonId, status);
    }



}

package vn.edu.fpt.service.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.dto.quizdto.QuizAnswerDTO;
import vn.edu.fpt.dto.quizdto.QuizQuestionDTO;
import vn.edu.fpt.entity.Quiz;
import vn.edu.fpt.entity.QuizAnswer;
import vn.edu.fpt.entity.QuizQuestion;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.QuizQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizQuestionService {
    private final QuizQuestionRepository repository;
    private final DtoMapper dtoMapper;
    private final QuizService quizService;

    public QuizQuestionService(QuizQuestionRepository quizQuestionRepository,
                               DtoMapper dtoMapper,
                               QuizService quizService) {
        this.repository = quizQuestionRepository;
        this.dtoMapper = dtoMapper;
        this.quizService = quizService;
    }

    public List<QuizQuestion> findAll() {
        return repository.findAll();
    }

    public Optional<QuizQuestion> findById(Integer id) {
        return repository.findById(id);
    }

    public QuizQuestion save(QuizQuestion entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public void createQuestion(QuizQuestionDTO dto, Integer quizId, User instructor) {
        Quiz quiz = quizService.getInstructorOwnedQuiz(quizId, instructor);

        Integer maxPosition =
                repository.findMaxPositionByQuizId(quizId);

        QuizQuestion question = new QuizQuestion();

        question.setQuiz(quiz);
        question.setQuestionText(dto.getQuestionText());
        question.setQuestionType(dto.getQuestionType());
        question.setPoints(dto.getPoints());
        question.setExplanation(dto.getExplanation());

        question.setPosition(
                maxPosition == null ? 1 : maxPosition + 1
        );

        for (int i = 0; i < dto.getAnswers().size(); i++) {

            QuizAnswerDTO answerDTO = dto.getAnswers().get(i);

            QuizAnswer answer = new QuizAnswer();

            answer.setAnswerText(answerDTO.getAnswerText());
            answer.setCorrect(Boolean.TRUE.equals(answerDTO.getCorrect()));

            answer.setPosition(i + 1);

            question.addAnswer(answer);
        }

        repository.save(question);
    }

    public void updateQuestion(QuizQuestionDTO dto, Integer quizId, User instructor) {

        QuizQuestion question = repository
                .findById(dto.getId())
                .orElseThrow();
        requireQuestionInOwnedQuiz(question, quizId, instructor);

        question.setQuestionText(dto.getQuestionText());
        question.setQuestionType(dto.getQuestionType());
        question.setPoints(dto.getPoints());
        question.setExplanation(dto.getExplanation());

        question.getAnswers().clear();

        for (int i = 0; i < dto.getAnswers().size(); i++) {

            QuizAnswerDTO answerDTO = dto.getAnswers().get(i);

            QuizAnswer answer = new QuizAnswer();

            answer.setAnswerText(answerDTO.getAnswerText());
            answer.setCorrect(Boolean.TRUE.equals(answerDTO.getCorrect()));

            answer.setPosition(i + 1);

            question.addAnswer(answer);
        }

        repository.save(question);
    }

    public void saveQuestion(QuizQuestionDTO quizQuestionDto, Integer quizId, User instructor){
        if(quizQuestionDto.getId() == null){
            createQuestion(quizQuestionDto, quizId, instructor);
        }
        else{
            updateQuestion(quizQuestionDto, quizId, instructor);
        }
    }

    public Page<QuizQuestionDTO> getQuestionsByQuizId(Integer quizId, int page, int size, User instructor) {
        quizService.getInstructorOwnedQuiz(quizId, instructor);

        Pageable pageable = PageRequest.of(page, size);

        Page<QuizQuestion> quizQuestionPage =
                repository.findAllByQuizIdOrderByPositionAsc(quizId, pageable);

        List<QuizQuestionDTO> dtos = new ArrayList<>();
        for (QuizQuestion quizQ : quizQuestionPage.getContent()){
            dtos.add(dtoMapper.toQuizQuestionDto(quizQ));
        }

        return new PageImpl<>(
                dtos,
                quizQuestionPage.getPageable(),
                quizQuestionPage.getTotalElements()
        );
    }

    public void deleteQuestion(Integer questionId, Integer quizId, User instructor){

        QuizQuestion question =
                repository.findQuizQuestionById(questionId);

        if(question == null){
            return;
        }
        requireQuestionInOwnedQuiz(question, quizId, instructor);

        Integer ownedQuizId = question.getQuiz().getId();
        Integer position = question.getPosition();

        repository.delete(question);

        repository.decreasePositionsAfter(
                ownedQuizId,
                position
        );
    }

    public Integer copyQuestion(Integer questionId, Integer quizId, User instructor){

        QuizQuestion original =
                repository.findQuizQuestionById(questionId);

        if(original == null){
            return null;
        }
        requireQuestionInOwnedQuiz(original, quizId, instructor);

        Integer newPosition =
                original.getPosition() + 1;

        repository.increasePositionsAfter(
                original.getQuiz().getId(),
                original.getPosition()
        );

        QuizQuestion copy = new QuizQuestion();

        copy.setQuiz(original.getQuiz());
        copy.setQuestionText(original.getQuestionText());
        copy.setQuestionType(original.getQuestionType());
        copy.setPoints(original.getPoints());
        copy.setExplanation(original.getExplanation());

        copy.setPosition(newPosition);

        for(QuizAnswer answer : original.getAnswers()){

            QuizAnswer answerCopy = new QuizAnswer();

            answerCopy.setAnswerText(answer.getAnswerText());
            answerCopy.setCorrect(answer.getCorrect());
            answerCopy.setPosition(answer.getPosition());

            copy.addAnswer(answerCopy);
        }

        repository.save(copy);

        return newPosition;
    }

    public QuizQuestionDTO findQuizQuestionById(Integer quizQuestionId, Integer quizId, User instructor){
        QuizQuestion question = repository.findQuizQuestionById(quizQuestionId);
        if (question == null) {
            return null;
        }
        requireQuestionInOwnedQuiz(question, quizId, instructor);
        return dtoMapper.toQuizQuestionDto(question);
    }

    public Integer getTotalQuestionsByQuizId(Integer quizId){
        return repository.countByQuizId(quizId);
    }

    private void requireQuestionInOwnedQuiz(QuizQuestion question, Integer quizId, User instructor) {
        Integer actualQuizId = question.getQuiz().getId();
        if (!actualQuizId.equals(quizId)) {
            throw new AccessDeniedException("Câu hỏi không thuộc quiz được yêu cầu.");
        }
        quizService.getInstructorOwnedQuiz(actualQuizId, instructor);
    }

}

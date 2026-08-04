package vn.edu.fpt.service.quiz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.QuizAnswer;
import vn.edu.fpt.repository.QuizAnswerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizAnswerService {
    private final QuizAnswerRepository repository;

    public QuizAnswerService(QuizAnswerRepository quizAnswerRepository) {
        this.repository = quizAnswerRepository;
    }

    public List<QuizAnswer> findAll() { return repository.findAll(); }
    public Optional<QuizAnswer> findById(Integer id) { return repository.findById(id); }
    public QuizAnswer save(QuizAnswer entity) { return repository.save(entity); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public boolean existsById(Integer id) { return repository.existsById(id); }
}

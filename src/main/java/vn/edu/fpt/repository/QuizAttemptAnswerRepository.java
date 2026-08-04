package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.QuizAttemptAnswer;

import java.util.List;

@Repository
public interface QuizAttemptAnswerRepository extends JpaRepository<QuizAttemptAnswer, Integer> {
    List<QuizAttemptAnswer> findByAttemptId(Integer attemptId);
    List<QuizAttemptAnswer> findByAttemptIdIn(List<Integer> attemptIds);
}

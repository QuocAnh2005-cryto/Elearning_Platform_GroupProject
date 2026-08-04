package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.QuizAnswer;
@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Integer> {
}

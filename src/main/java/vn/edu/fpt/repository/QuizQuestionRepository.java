package vn.edu.fpt.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.QuizQuestion;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {

    @Query("""
       select max(q.position)
       from QuizQuestion q
       where q.quiz.id = :quizId
       """)
    Integer findMaxPositionByQuizId(
            @Param("quizId") Integer quizId
    );

    Page<QuizQuestion> findAllByQuizIdOrderByPositionAsc(Integer quizId, Pageable pageable);

    QuizQuestion findQuizQuestionById(Integer questionId);

    Integer countByQuizId(Integer quizId);

    @Modifying
    @Query("""
    update QuizQuestion q
    set q.position = q.position - 1
    where q.quiz.id = :quizId
      and q.position > :position
""")
    void decreasePositionsAfter(
            @Param("quizId") Integer quizId,
            @Param("position") Integer position
    );

    @Modifying
    @Query("""
    update QuizQuestion q
    set q.position = q.position + 1
    where q.quiz.id = :quizId
      and q.position > :position
""")
    void increasePositionsAfter(
            @Param("quizId") Integer quizId,
            @Param("position")Integer position
    );


}

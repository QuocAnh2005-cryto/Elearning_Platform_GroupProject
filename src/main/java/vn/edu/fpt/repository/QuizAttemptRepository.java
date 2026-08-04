package vn.edu.fpt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.QuizAttempt;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {
    List<QuizAttempt> findByUserIdAndQuizIdOrderBySubmittedAtDesc(Integer userId, Integer quizId);

    // 1. Tìm tất cả theo QuizId (Không từ khóa, không trạng thái)
    Page<QuizAttempt> findAllByQuizId(Integer quizId, Pageable pageable);

    // 2. Tìm theo QuizId + Trạng thái Đạt/Trượt (Không từ khóa)
    Page<QuizAttempt> findAllByQuizIdAndPassed(Integer quizId, Boolean passed, Pageable pageable);

    // 3. Tìm theo QuizId + Từ khóa (Không quan tâm trạng thái)
    @Query("""
            SELECT qa
            FROM QuizAttempt qa
            WHERE qa.quiz.id = :quizId
            AND (
                LOWER(CONCAT(qa.user.firstName, ' ', qa.user.lastName))
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(qa.user.email)
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    Page<QuizAttempt> searchAttempts(
            @Param("quizId") Integer quizId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 4. Tìm kiếm ĐẦY ĐỦ: QuizId + Từ khóa + Trạng thái Đạt/Trượt
    @Query("""
            SELECT qa
            FROM QuizAttempt qa
            WHERE qa.quiz.id = :quizId
            AND qa.passed = :passed
            AND (
                LOWER(CONCAT(qa.user.firstName, ' ', qa.user.lastName))
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(qa.user.email)
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    Page<QuizAttempt> searchAttemptsWithStatus(
            @Param("quizId") Integer quizId,
            @Param("keyword") String keyword,
            @Param("passed") Boolean passed,
            Pageable pageable
    );
}

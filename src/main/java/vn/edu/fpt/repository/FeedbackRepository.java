package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Feedback;
import java.util.Optional;


import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);
    Optional<Feedback> findByUserIdAndCourseId(Integer userId, Integer courseId);
    long countByRatingGreaterThanEqual(int rating);

    // Instructor course list: lấy đánh giá của đúng khóa học để màn "Xem đánh giá" hiển thị.
    @EntityGraph(attributePaths = "user")
    List<Feedback> findByCourseIdOrderByCreatedAtDesc(Integer courseId);

    // Instructor profile: tong hop rating cua tat ca khoa hoc thuoc instructor dang xem profile.
    @Query("""
            select coalesce(avg(f.rating), 0.0)
            from Feedback f
            where f.course.instructor.id = :instructorId
              and f.rating is not null
              and (f.status is null or f.status = vn.edu.fpt.enums.FeedbackStatus.VISIBLE)
            """)
    Double getAverageRatingByInstructorId(@Param("instructorId") Integer instructorId);

    // Instructor profile: dem so luot danh gia hop le de hien thi kem diem trung binh.
    @Query("""
            select count(f.id)
            from Feedback f
            where f.course.instructor.id = :instructorId
              and f.rating is not null
              and (f.status is null or f.status = vn.edu.fpt.enums.FeedbackStatus.VISIBLE)
            """)
    long countRatingsByInstructorId(@Param("instructorId") Integer instructorId);
}

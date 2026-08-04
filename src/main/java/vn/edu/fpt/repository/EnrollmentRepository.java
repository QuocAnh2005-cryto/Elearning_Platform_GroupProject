package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Enrollment;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.entity.Course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByUser(User user);

    boolean existsByUserAndCourse(User user, Course course);

    long countByCourseId(Integer courseId);


    @Query("""
                select e from Enrollment e where e.course.id = :courseId and e.user.id = :userId
            """)
    Optional<Enrollment> findByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    @Query("""
           select count(distinct e.user.id) 
           from Enrollment e
           where e.course.instructor.id = :instructorId
           and e.createdAt between :fromDate and :toDate   
           """)
    long countDistictStudents(@Param("instructorId") Integer instructorId,
                              @Param("fromDate") LocalDateTime fromDate,
                              @Param("toDate") LocalDateTime toDate);
}

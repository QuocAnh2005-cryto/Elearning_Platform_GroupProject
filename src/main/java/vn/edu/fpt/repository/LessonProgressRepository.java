package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Enrollment;
import vn.edu.fpt.entity.LessonProgress;

import java.util.List;
import java.util.Set;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {

    @Query("""
                    select count(lp.id) from LessonProgress lp where lp.enrollment.id = :enrollmentId and lp.completed = true 
            """)
    Integer findNumberOfLessonCompletedByEnrollment(@Param("enrollmentId") Integer enrollment);

    @Query("""
               select lp from LessonProgress lp where lp.enrollment.id = :id and lp.lesson.id = :lessonId
            """)
    LessonProgress findByEnrollmentIdAndLessonId(@Param("id") Integer id, @Param("lessonId") Integer lessonId);

    @Query("""
        select lp.completed from LessonProgress lp where lp.lesson.id = :lessonId
            """)
    Boolean findStatusByLessonId(@Param("lessonId") Integer lessonId);


    @Query("""
      select lp.lesson.id from LessonProgress lp where lp.enrollment.user.id = :userId and lp.enrollment.course.id = :courseId and lp.completed = TRUE 
""")
    Set<Integer> findByUserIdAndCourseId(@Param("userId") Integer id, @Param("courseId") Integer courseId);
}

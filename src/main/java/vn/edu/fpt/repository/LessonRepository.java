package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.lesson.LessonSiderbarDTO;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonMaterial;
import vn.edu.fpt.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
        boolean existsByTitleAndCourseSection_Id(String title, Integer sectionId);

        // Lesson validation: khong cho trung ten lesson trong cung mot section.
        @Query("""
                        select case when count(l) > 0 then true else false end
                        from Lesson l
                        where l.courseSection.id = :sectionId
                          and lower(trim(l.title)) = lower(trim(:title))
                          and (:excludeLessonId is null or l.id <> :excludeLessonId)
                        """)
        boolean existsDuplicateTitleInSection(@Param("sectionId") Integer sectionId,
                                              @Param("title") String title,
                                              @Param("excludeLessonId") Integer excludeLessonId);

        Optional<Lesson> findFirstByCourseSection_IdOrderByPositionAscIdAsc(Integer sectionId);

        @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.materials WHERE l.id = :id")
        Optional<Lesson> findByIdWithMaterials(@Param("id") Integer id);

        @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.quizzes WHERE l.id = :id")
        Optional<Lesson> findByIdWithQuizzes(@Param("id") Integer id);

        Lesson getFinalCompledLessonIdByCourseSection(CourseSection courseSection);

        @Query("""
                        select lp.lesson.id from LessonProgress lp left join lp.enrollment e
                                    where e.course.id = :courseId and e.user.id = :userId
                                                and lp.completed = true
                                                order by lp.lesson.id desc
                        """)
        List<Integer> getCompletedLessonIdByCourseIdAndUserId(@Param("courseId") Integer id,
                        @Param("userId") Integer id1);

        @Query("""
                        select l.courseSection.id from Lesson l where l.id = :id
                        """)
        Integer findSectionIdByLessonId(@Param("id") Integer lessonIdFinalCompleted);

        @Query("""
                                    select min(l.id) from Lesson l where l.courseSection.course.id = :id
                        """)
        Integer findFirstLessonIdByCourseId(@Param("id") Integer attr2);

        @Query("""
                                select count(l.id) from Lesson l where l.courseSection.course.id = :courseId
                        """)
        Integer findNumberOfLessonByCourseId(@Param("courseId") Integer courseId);

        @Query("""
                                select l from Lesson l where l.courseSection.course.id = :courseId
                                and l.id not in (select lp.lesson.id from LessonProgress lp where lp.enrollment.user.id = :#{#user.id} and lp.completed = true )
                        """)
        List<Lesson> findNotCompletedLessons(@Param("user") User user, @Param("courseId") Integer courseId);

        @Query("""
                            SELECT l
                            FROM Lesson l
                            JOIN FETCH l.courseSection s
                            JOIN FETCH s.course
                            WHERE l.id = :lessonId
                        """)
        Lesson findDetailById(Integer lessonId);

        @Query("SELECT COALESCE(MAX(l.position), 0) FROM Lesson l WHERE l.courseSection.id = :sectionId")
        Integer findMaxPositionLesson(@Param("sectionId") Integer sectionId);

        @Query("""
                        SELECT COALESCE(MAX(l.position), 0) FROM Lesson l where l.courseSection.id = :sectionId
                        """)
        Integer FindMaxPositionByCourseSectionId(@Param("sectionId") Integer sectionId);

        @Query("""
                                select new vn.edu.fpt.dto.lesson.LessonSiderbarDTO(l.id, l.durationSeconds, l.title, l.position, l.videoUrl) from Lesson l where l.courseSection.id = :id order by l.position asc
                        """)
        List<LessonSiderbarDTO> findLessonBySecionId(@Param("id") Integer id);

       //Check trùng tên bài hoọc
        @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
                "FROM Lesson l " +
                "WHERE l.title = :title " +
                "AND l.courseSection.id = :sectionId " +
                "AND l.id != :excludeLessonId")
        boolean existsByTitleAndCourseSection_IdAndIdNot(
                @Param("title") String title,
                @Param("sectionId") Integer sectionId,
                @Param("excludeLessonId") Integer excludeLessonId
        );



}

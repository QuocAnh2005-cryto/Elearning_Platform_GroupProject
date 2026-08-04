package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.dto.lesson.SectionSiderbarDTO;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.Lesson;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {
    Optional<CourseSection> findFirstByCourse_IdOrderByPositionAscIdAsc(Integer courseId);

    CourseSection save(CourseSection courseSection);

    @Query("""
                select cs.course.id from CourseSection cs where cs.id = :id
            """)
    Optional<Integer> findBySectionId(@Param("id") Integer sectionId);

    CourseSection findCourseSectionById(Integer courseSectionId);

    @Query("""
            SELECT COALESCE(MAX(s.position), 0) from CourseSection s where s.course.id = :courseid
             """)
    Integer FindMaxPositionByCourseId(@Param("courseid") Integer courseId);

    List<CourseSection> findByCourseId(Integer courseId);

    // Section validation: khong cho trung ten section trong cung mot course.
    @Query("""
            select case when count(s) > 0 then true else false end
            from CourseSection s
            where s.course.id = :courseId
              and lower(trim(s.title)) = lower(trim(:title))
              and (:excludeSectionId is null or s.id <> :excludeSectionId)
            """)
    boolean existsDuplicateTitleInCourse(@Param("courseId") Integer courseId,
                                         @Param("title") String title,
                                         @Param("excludeSectionId") Integer excludeSectionId);

    @Query("""
            select c from CourseSection c LEFT JOIN FETCH c.lessons l where c.course.id = :courseId Order by c.position
            """)
    List<CourseSection> findByCourseAndLesson(@Param("courseId") Integer courseId);

    @Query("""
                   select new vn.edu.fpt.dto.lesson.SectionSiderbarDTO(cs.id, cs.title, cs.position) from CourseSection cs where cs.course.id = :courseId order by cs.position asc
            """)
    List<SectionSiderbarDTO> findSectionSiderbarDTOByCourseId(@Param("courseId") Integer courseId);


    List<CourseSection> findByCourseIdOrderByPosition(Integer courseId);
}

package vn.edu.fpt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonMaterial;
import java.util.List;
import java.util.Optional;
@Repository
public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, Integer> {
    Optional<LessonMaterial> findFirstByLesson_IdOrderByIdAsc(Integer lessonId);


    @Query("""
            select distinct m
            from LessonMaterial m
            join fetch m.lesson l
            join fetch l.courseSection s
            join fetch s.course c
            left join fetch c.category
            where c.instructor.id = :instructorId
            order by m.createdAt desc, m.id desc
            """)
    List<LessonMaterial> findLibraryByInstructorId(@Param("instructorId") Integer instructorId);


    @Query(value = """
            select distinct m
            from LessonMaterial m
            join fetch m.lesson l
            join fetch l.courseSection s
            join fetch s.course c
            where c.instructor.id = :instructorId
              and (:keyword is null or :keyword = '' or lower(m.fileName) like lower(concat('%', :keyword, '%')))
              and (:courseId is null or c.id = :courseId)
              and (:sectionId is null or s.id = :sectionId)
              and (:lessonId is null or l.id = :lessonId)
              and (:fileType is null or :fileType = '' or lower(m.fileType) = lower(:fileType))
            order by m.createdAt desc, m.id desc
            """,
            countQuery = """
            select count(distinct m.id)
            from LessonMaterial m
            join m.lesson l
            join l.courseSection s
            join s.course c
            where c.instructor.id = :instructorId
              and (:keyword is null or :keyword = '' or lower(m.fileName) like lower(concat('%', :keyword, '%')))
              and (:courseId is null or c.id = :courseId)
              and (:sectionId is null or s.id = :sectionId)
              and (:lessonId is null or l.id = :lessonId)
              and (:fileType is null or :fileType = '' or lower(m.fileType) = lower(:fileType))
            """)
    Page<LessonMaterial> searchLibraryByInstructorId(@Param("instructorId") Integer instructorId,
                                                     @Param("keyword") String keyword,
                                                     @Param("courseId") Integer courseId,
                                                     @Param("sectionId") Integer sectionId,
                                                     @Param("lessonId") Integer lessonId,
                                                     @Param("fileType") String fileType,
                                                     Pageable pageable);

    @Query("""
            select distinct c
            from LessonMaterial m
            join m.lesson l
            join l.courseSection s
            join s.course c
            where c.instructor.id = :instructorId
            order by c.title asc
            """)
    List<Course> findLibraryCoursesByInstructorId(@Param("instructorId") Integer instructorId);

    @Query("""
            select distinct s
            from LessonMaterial m
            join m.lesson l
            join l.courseSection s
            join s.course c
            where c.instructor.id = :instructorId
              and (:courseId is null or c.id = :courseId)
            order by s.position asc, s.title asc
            """)
    List<CourseSection> findLibrarySectionsByInstructorId(@Param("instructorId") Integer instructorId,
                                                          @Param("courseId") Integer courseId);

    @Query("""
            select distinct l
            from LessonMaterial m
            join m.lesson l
            join l.courseSection s
            join s.course c
            where c.instructor.id = :instructorId
              and (:courseId is null or c.id = :courseId)
              and (:sectionId is null or s.id = :sectionId)
            order by l.position asc, l.title asc
            """)
    List<Lesson> findLibraryLessonsByInstructorId(@Param("instructorId") Integer instructorId,
                                                  @Param("courseId") Integer courseId,
                                                  @Param("sectionId") Integer sectionId);

    @Query("""
            select distinct lower(m.fileType)
            from LessonMaterial m
            join m.lesson l
            join l.courseSection s
            join s.course c
            where c.instructor.id = :instructorId
              and m.fileType is not null
            order by lower(m.fileType) asc
            """)
    List<String> findLibraryFileTypesByInstructorId(@Param("instructorId") Integer instructorId);

    @Query("""
            select distinct m
            from LessonMaterial m
            join fetch m.lesson l
            join fetch l.courseSection s
            join fetch s.course c
            where m.id = :materialId
              and c.instructor.id = :instructorId
            """)
    Optional<LessonMaterial> findOwnedMaterialForDelete(@Param("materialId") Integer materialId,
                                                       @Param("instructorId") Integer instructorId);
}

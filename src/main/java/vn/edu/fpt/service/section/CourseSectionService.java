package vn.edu.fpt.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import vn.edu.fpt.dto.*;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.QuizStatus;
import vn.edu.fpt.exception.CourseSectionValidation;
import vn.edu.fpt.exception.CourseNotFoundException;
import vn.edu.fpt.exception.CourseValidationException;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.repository.CourseSectionRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class CourseSectionService {
    private final CourseSectionRepository repository;
    private final DtoMapper dtoMapper;
    private final CourseRepository courseRepository;


    public CourseSectionService(CourseSectionRepository courseSectionRepository, DtoMapper dtoMapper, CourseRepository courseRepository) {
        this.repository = courseSectionRepository;
        this.dtoMapper = dtoMapper;
        this.courseRepository = courseRepository;
    }

    public List<CourseSection> findAll() {
        return repository.findAll();
    }

    public Optional<CourseSection> findById(Integer id) {
        return repository.findById(id);
    }


    public CourseSection save(CourseSection entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteSection(Integer sectionId, User user) {

        CourseSection deleted = repository.findById(sectionId).orElseThrow();
        Course course = deleted.getCourse();
        if (user == null || course == null || course.getInstructor() == null || !course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa chương này.");
        }
        Integer deletedPos = deleted.getPosition();
        Integer courseId = course.getId();

        repository.delete(deleted);

        List<CourseSection> list =
                repository.findByCourseIdOrderByPosition(courseId);

        for (CourseSection s : list) {
            if (s.getPosition() > deletedPos) {
                s.setPosition(s.getPosition() - 1);
            }
        }
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public int totalLesson(List<CourseSectionDto> listSection){
        if(listSection == null) return 0;
        return listSection.stream().mapToInt(s -> s.getLessons().size()).sum();
    }


        public void updateCourseSection(Integer sectionId, CourseSectionDto courseSectionDto){
           CourseSection courseSection = repository.findById(sectionId).orElseThrow();
           String normalizedTitle = courseSectionDto.getTitle() != null ? courseSectionDto.getTitle().trim() : "";
           // Section validation: khong cho trung ten section trong cung course khi edit.
           if (repository.existsDuplicateTitleInCourse(courseSection.getCourse().getId(), normalizedTitle, sectionId)) {
               throw new CourseSectionValidation("Tên chương này đã tồn tại trong khóa học.", "title");
           }
           courseSection.setTitle(normalizedTitle);
           courseSection.setUpdatedAt(LocalDateTime.now());
           repository.save(courseSection);
        }

    public Set<CourseSection> findSectionsByCourse(Course course) {
        if (course.getSections() == null || course.getSections().isEmpty()) {
            throw new CourseNotFoundException("Khóa học không có section nào");
        }
        return course.getSections();
    }

    public Integer findCourseIdBySectionId(Integer sectionId) {
        return repository.findBySectionId(sectionId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khóa học nào với Section id là " + sectionId));
    }

    public Map<Integer, Boolean> getSectionCompletedMap(Set<CourseSectionDto> sections, List<Integer> completedLessonIds) {
        Map<Integer, Boolean> sectionCompletedMap = new HashMap<>();
        if (sections != null) {
            for (CourseSectionDto sectionDto : sections) {
                boolean allCompleted = true;
                if (sectionDto.getLessons() == null || sectionDto.getLessons().isEmpty()) {
                    allCompleted = false;
                } else {
                    for (LessonDto l : sectionDto.getLessons()) {
                        if (completedLessonIds == null || !completedLessonIds.contains(l.getId())) {
                            allCompleted = false;
                            break;
                        }
                    }
                }
                sectionCompletedMap.put(sectionDto.getId(), allCompleted);
            }
        }
        return sectionCompletedMap;
    }

    ////Thêm Section vào Khoá Học hiện tại
    public CourseSection SaveSection(CourseSectionDto courseSectionDto, Course course){
        if(course == null){
            throw new CourseValidationException("courseRequets","Khoá học này không tồn tại");
        }

        String normalizedTitle = courseSectionDto.getTitle() != null ? courseSectionDto.getTitle().trim() : "";
        // Section validation: khong cho trung ten section trong cung course khi tao moi.
        if (repository.existsDuplicateTitleInCourse(course.getId(), normalizedTitle, null)) {
            throw new CourseSectionValidation("Tên chương này đã tồn tại trong khóa học.", "title");
        }

        Integer po = repository.FindMaxPositionByCourseId(course.getId());
        CourseSection courseSection = new CourseSection();
        courseSection.setTitle(normalizedTitle);
        courseSection.setPosition(po + 1);
        courseSection.setCourse(course);
        courseSection.setCreatedAt(LocalDateTime.now());
        return repository.save(courseSection);
    }
    //// Danh sách Section theo Course
    public List<CourseSectionDto> FindSectionByCourseId(Integer courseId){
        List<CourseSection> courseSections = repository.findByCourseId(courseId);
        List<CourseSectionDto> courseSectionDtos  = new ArrayList<>();
        for(CourseSection c : courseSections){
            CourseSectionDto courseSectionDto = new CourseSectionDto();

            courseSectionDto.setId(c.getId());
            courseSectionDto.setTitle(c.getTitle());
            courseSectionDto.setPosition(c.getPosition());

            courseSectionDtos.add(courseSectionDto);
        }
         return courseSectionDtos;
    }

    public List<CourseSectionDto> findByCourseAndLesson(Integer courseId){
        List<CourseSection> courseSections = repository.findByCourseAndLesson(courseId);
        List<CourseSectionDto> courseSectionDtos = new ArrayList<>();
        Course tmp = courseRepository.findCourseById(courseId);
        CourseCreateDto courseDto = new CourseCreateDto();
        courseDto.setId(tmp.getId());
        for(CourseSection c : courseSections){
            CourseSectionDto courseSectionDto = new CourseSectionDto();

            courseSectionDto.setId(c.getId());
            courseSectionDto.setTitle(c.getTitle());
            courseSectionDto.setPosition(c.getPosition());
            courseSectionDto.setCourse(courseDto);

            List<LessonDto> lessons = c.getLessons().stream().
                    map(l -> {
                        LessonDto lessonDto = new LessonDto();
                        lessonDto.setId(l.getId());
                        lessonDto.setTitle(l.getTitle());
                        lessonDto.setPosition(l.getPosition());
                        lessonDto.setDurationSeconds(l.getDurationSeconds());
                        lessonDto.setQuizzes(
                                l.getQuizzes().stream()
                                        .filter(q -> QuizStatus.PUBLISHED.name().equalsIgnoreCase(q.getStatus()))
                                        .map(q -> LessonQuizDto.builder()
                                        .id(q.getId())
                                        .title(q.getTitle())
                                        .status(q.getStatus())
                                        .questionCount(q.getQuestions() != null ? q.getQuestions().size() : 0)
                                        .build()
                                ).toList()
                        );
                        lessonDto.setMaterials(
                                l.getMaterials().stream()
                                        .map(m -> LessonMaterialDto.builder()
                                                .id(m.getId())
                                                .fileName(m.getFileName())
                                                .fileUrl(m.getFileUrl())
                                                .fileType(m.getFileType())
                                                .fileSize(m.getFileSize())
                                                .build()
                                        )
                                        .toList()
                        );
                        return lessonDto;
                    }).toList();

            courseSectionDto.setLessons(lessons);

            courseSectionDtos.add(courseSectionDto);

        }
        return courseSectionDtos;
    }







    public CourseSection findBySectionId(Integer courseSectionId){
        return repository.findCourseSectionById(courseSectionId);
    }

    public CourseSectionDto findByCourseSectionId(Integer courseSectionId){
        return dtoMapper.toCourseSectionDto(repository.findCourseSectionById(courseSectionId));
    }
}

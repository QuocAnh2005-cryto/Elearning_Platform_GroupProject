package vn.edu.fpt.service.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.dto.LessonDto;
import vn.edu.fpt.entity.CourseSection;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.enums.LessonModerationStatus;
import vn.edu.fpt.exception.CourseNotFoundException;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.LessonRepository;
import vn.edu.fpt.service.material.LessonMaterialService;
import vn.edu.fpt.service.material.LessonMaterialService;
import vn.edu.fpt.service.section.CourseSectionService;
import vn.edu.fpt.service.cloud.AzureBlobService;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.util.SecurityUtils;

import vn.edu.fpt.repository.EnrollmentRepository;
import vn.edu.fpt.entity.Course;
import vn.edu.fpt.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository repository;
    private final AzureBlobService azureBlobService;
    private final DtoMapper dtoMapper;
    private final CourseSectionService courseSectionService;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonMaterialService lessonMaterialService;

    public boolean hasAccessToLesson(User user, Lesson lesson) {
        if (user == null || lesson == null || lesson.getCourseSection() == null) {
            return false;
        }
        Course course = lesson.getCourseSection().getCourse();
        RoleType role = user.getRole();
        if (role == RoleType.ADMIN || role == RoleType.MANAGER) {
            return true;
        }
        if (role == RoleType.INSTRUCTOR) {
            return course.getInstructor() != null && course.getInstructor().getId().equals(user.getId());
        }
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }


    public List<Lesson> findAll() {
        return repository.findAll();
    }

    public Optional<Lesson> findById(Integer id) {
        return repository.findById(id);
    }

    public Lesson findByIdWithMaterials(Integer id) {
        return repository.findByIdWithMaterials(id).orElseThrow(() -> new CourseNotFoundException("Bài học không tìm thấy"));
    }

    @Transactional(readOnly = true)
    public Lesson findByIdWithQuizzes(Integer id) {
        return repository.findByIdWithQuizzes(id).orElseThrow(() -> new ResourceNotFoundException("Lesson with id " + id + " not found"));
    }

    public Lesson save(Lesson entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    private Integer findMaxPositionLesson(Integer sectionId){
        return repository.FindMaxPositionByCourseSectionId(sectionId);
    }

    public Lesson saveLesson(Integer sectiondId, LessonDto lessonDto, MultipartFile file, String videoBlobName){
        boolean exist = courseSectionService.existsById(sectiondId);
        if(!exist){
            throw new RuntimeException("Tiêu đề khoá học không tìm thấy với id: " + sectiondId);
        }

        if(lessonDto == null){
            throw new RuntimeException("Dữ liệu bài học không tồn tại");
        }

        String videoUrl;
        if(videoBlobName != null && !videoBlobName.isBlank()){
            videoUrl = videoBlobName;
        }else{
            if(file == null || file.isEmpty()){
                throw new RuntimeException("Video bài học không được để trông");
            }

            videoUrl = azureBlobService.saveFile(file, AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS);
        }

        String normalizedTitle = lessonDto.getTitle() != null ? lessonDto.getTitle().trim() : "";

        if(repository.existsDuplicateTitleInSection(sectiondId, normalizedTitle, null)){
            throw new RuntimeException("Tiêu đề bài này đã được thiết lập");
        }

        if(normalizedTitle.isEmpty()){
            throw new RuntimeException("Tiêu đề bài học không được để trống");
        }
        if(normalizedTitle.length() < 5){
            throw new RuntimeException("Tiêu đề bài học phải đạt tối thiểu 5 kí tự");
        }
        if(normalizedTitle.length() > 255){
            throw new RuntimeException("Tiêu đề bài học đã dài quá mức cho phép");
        }

        if((videoBlobName == null || videoBlobName.isBlank()) && (file == null || file.isEmpty())){
            throw new RuntimeException("Video bài học không được để trống");
        }

        if(lessonDto.getDurationSeconds() == null || lessonDto.getDurationSeconds() <= 0){
            lessonDto.setDurationSeconds(0);
        }

        Integer po = repository.findMaxPositionLesson(sectiondId);  

        Lesson l = new Lesson();
        l.setTitle(normalizedTitle);
        l.setVideoUrl(videoUrl);
        l.setPosition(po + 1);
        l.setDurationSeconds(lessonDto.getDurationSeconds());
        l.setCreatedAt(LocalDateTime.now());
        l.setPublished(false);

        l.setModerationStatus(LessonModerationStatus.PENDING.toString());
        l.setCourseSection(courseSectionService.findById(sectiondId).orElseThrow());
        return repository.save(l);
    }

    public Set<Lesson> findLessonByCourseSection(CourseSection courseSection) {
        if (courseSection.getLessons() == null || courseSection.getLessons().isEmpty()) {
            throw new CourseNotFoundException("Section không có bài học nào");
        }
        return courseSection.getLessons();
    }

    public Integer findLessonIdFinalCompletedByCourseIdAndUserId(Integer id, Integer id1) {
        List<Integer> lessonId = repository.getCompletedLessonIdByCourseIdAndUserId(id, id1);
        if (lessonId.isEmpty()) {
           return repository.findFirstLessonIdByCourseId(id);
        }
        return lessonId.get(0);
    }

    public List<Integer> getCompletedLessonIdsByCourseIdAndUserId(Integer courseId, Integer userId) {
        return repository.getCompletedLessonIdByCourseIdAndUserId(courseId, userId);
    }

    public Integer findSectionIdByLessonId(Integer lessonIdFinalCompleted) {
        if (lessonIdFinalCompleted == null) {
            return null;
        }
        return repository.findSectionIdByLessonId(lessonIdFinalCompleted);
    }

    public Integer findNumberOfLessonByCourseId(Integer courseId) {
        return repository.findNumberOfLessonByCourseId(courseId);
    }

    public String findLessonUrl(Integer lessonId) {
        try {
            User user = SecurityUtils.getCurrentUser();
            if (user == null) {
                return null;
            }
            Lesson lesson = findById(lessonId).orElse(null);
            if (lesson == null) {
                return null;
            }
            if (!hasAccessToLesson(user, lesson)) {
                return null;
            }
            if (lesson.getVideoUrl() == null || lesson.getVideoUrl().trim().isEmpty()) {
                return "https://www.w3schools.com/html/mov_bbb.mp4";
            }
            return azureBlobService.generateSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, lesson.getVideoUrl());
        } catch (Exception e) {
            return "https://www.w3schools.com/html/mov_bbb.mp4";
        }
    }

    public Lesson findNextLessonByCurrentLesson(Integer lessonId,Integer courseId, Integer totalNumberOfLesson, Integer totalNumberOfLessonCompleted) {
        if (totalNumberOfLesson == totalNumberOfLessonCompleted) {
            return null;
        }
        User user = SecurityUtils.getCurrentUser();
        List<Lesson> nextLessons = repository.findNotCompletedLessons(user, courseId);
        if (nextLessons == null || nextLessons.isEmpty()) {
            return null;
        }
        for (Lesson nextLesson : nextLessons) {
            if ((nextLesson.getId() > lessonId) || (lessonId == nextLessons.get(nextLessons.size() - 1).getId())) {
                return nextLesson;
            }
        }
        return nextLessons.get(0);
    }

    public LessonDto getLessonById(Integer lessonId){

        return dtoMapper.toLessonDto(repository.findDetailById(lessonId));
    }

    public Lesson findLessonById(Integer lessonId){
        return repository.findDetailById(lessonId);
    }

    public String findLessonUrl(Lesson lesson) {
        try {
            if (lesson == null || lesson.getVideoUrl() == null || lesson.getVideoUrl().trim().isEmpty()) {
                return "https://www.w3schools.com/html/mov_bbb.mp4";
            }
            return azureBlobService.generateSasUrl(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, lesson.getVideoUrl());
        } catch (Exception e) {
            return "https://www.w3schools.com/html/mov_bbb.mp4";
        }
    }

    public Lesson updateLesson(Integer lessonId, LessonDto lessonDto, MultipartFile videoFile, String blobName) {
        if (lessonId == null || lessonId <= 0) {
            throw new RuntimeException("ID bài giảng không hợp lệ");
        }

        Lesson lesson = repository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài giảng không tìm thấy với id: " + lessonId));

        String normalizedTitle = lessonDto.getTitle() != null ? lessonDto.getTitle().trim() : "";
        if (normalizedTitle.isEmpty()) {
            throw new RuntimeException("Tiêu đề bài học không được để trống");
        }
        if (normalizedTitle.length() > 255) {
            throw new RuntimeException("Tiêu đề bài học quá dài");
        }

        boolean titleExists = repository.existsDuplicateTitleInSection(
                lesson.getCourseSection().getId(),
                normalizedTitle,
                lessonId
        );
        if (titleExists) {
            throw new RuntimeException("Tiêu đề bài này đã tồn tại trong chương");
        }


        lesson.setTitle(normalizedTitle);

        if (blobName != null && !blobName.isBlank()) {
            String newBlobName = blobName.trim();
            String oldBlobName = lesson.getVideoUrl();
            if (oldBlobName != null && !oldBlobName.isEmpty() && !oldBlobName.equals(newBlobName)) {
                try {
                    azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, oldBlobName);
                } catch (Exception e) {
                    System.err.println("Không thể xóa video cũ: " + e.getMessage());
                }
            }
            lesson.setVideoUrl(newBlobName);
            if (lessonDto.getDurationSeconds() != null && lessonDto.getDurationSeconds() > 0) {
                lesson.setDurationSeconds(lessonDto.getDurationSeconds());
            }
        } else if (videoFile != null && !videoFile.isEmpty()) {
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                try {
                    azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, lesson.getVideoUrl());
                } catch (Exception e) {
                    System.err.println("Không thể xóa video cũ: " + e.getMessage());
                }
            }


            String newVideoUrl = azureBlobService.saveFile(videoFile, AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS);
            lesson.setVideoUrl(newVideoUrl);


            if (lessonDto.getDurationSeconds() != null && lessonDto.getDurationSeconds() > 0) {
                lesson.setDurationSeconds(lessonDto.getDurationSeconds());
            }
        } else {
            if (lessonDto.getDurationSeconds() != null && lessonDto.getDurationSeconds() > 0) {
                lesson.setDurationSeconds(lessonDto.getDurationSeconds());
            }
        }

        lesson.setModerationStatus(LessonModerationStatus.PENDING.toString());

        return repository.save(lesson);
    }


    public void deleteLesson(Integer lessonId, User user) {
        if (lessonId == null || lessonId <= 0) {
            throw new RuntimeException("ID bài giảng không hợp lệ");
        }

        Lesson lesson = repository.findByIdWithMaterials(lessonId).orElseThrow();
        if (lesson == null) {
            throw new ResourceNotFoundException("Bài giảng không tìm thấy với id: " + lessonId);
        }


        Course course = lesson.getCourseSection() != null ? lesson.getCourseSection().getCourse() : null;
        // Delete lesson ownership: chi instructor so huu course moi duoc xoa lesson trong course do.
        if (user == null || course == null || course.getInstructor() == null || !course.getInstructor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa bài giảng này.");
        }

        if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
            try {
                azureBlobService.deleteFile(AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS, lesson.getVideoUrl());
                System.out.println("✓ Xóa video thành công: " + lesson.getVideoUrl());
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Không thể xóa video: " + e.getMessage());
            }
        }


        if (lesson.getMaterials() != null && !lesson.getMaterials().isEmpty()) {
            new java.util.ArrayList<>(lesson.getMaterials()).forEach(material -> {
                try {
                    lessonMaterialService.deleteMaterialById(material.getId());
                } catch (Exception e) {
                    System.err.println("⚠️ Warning: Không thể xóa material: " + e.getMessage());
                }
            });
        }


        repository.deleteById(lessonId);
        System.out.println("✓ Xóa bài giảng thành công: " + lessonId);
    }
}

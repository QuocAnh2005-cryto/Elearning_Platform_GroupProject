package vn.edu.fpt.service.lesson;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.entity.Enrollment;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonProgress;
import vn.edu.fpt.repository.LessonProgressRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonProgressService {
    private final LessonProgressRepository repository;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository) {
        this.repository = lessonProgressRepository;
    }

    public List<LessonProgress> findAll() {
        return repository.findAll();
    }

    public Optional<LessonProgress> findById(Integer id) {
        return repository.findById(id);
    }

    public LessonProgress save(LessonProgress entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public void saveLessonProgressByEnrollmentAndLessonId(Enrollment enrollment, Integer lessonId) {
        LessonProgress lessonProgress = repository.findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId);
        if (lessonProgress == null) {
            lessonProgress = LessonProgress.builder()
                    .enrollment(enrollment)
                    .lesson(Lesson.builder().id(lessonId).build())
                    .completed(true)
                    .lastAccessed(LocalDateTime.now())
                    .build();
        } else {
            lessonProgress.setLastAccessed(LocalDateTime.now());
            lessonProgress.setCompleted(true);
        }
        repository.save(lessonProgress);
    }

    public Integer findNumberOfLessonCompletedByEnrollment(Enrollment enrollment) {
        return repository.findNumberOfLessonCompletedByEnrollment(enrollment.getId());
    }

    public Boolean findStatusByLessonId(Integer lessonId) {
        Boolean status = false;
        status = repository.findStatusByLessonId(lessonId);
        if (status == null) {
            status = false;
        }
        return status;
    }
}

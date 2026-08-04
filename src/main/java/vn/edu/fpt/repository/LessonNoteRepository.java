package vn.edu.fpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.entity.LessonNote;

import java.util.List;

public interface LessonNoteRepository extends JpaRepository<LessonNote, Integer> {
    List<LessonNote> findByUser_IdAndLesson_IdOrderByCreatedAtDesc(Integer userId, Integer lessonId);

    LessonNote findByUser_IdAndId(Integer userId, Integer id);

    List<LessonNote> findByUser_IdOrderByCreatedAtDesc(Integer userId);
}

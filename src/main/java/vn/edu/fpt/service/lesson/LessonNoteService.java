package vn.edu.fpt.service.lesson;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;
import vn.edu.fpt.dto.lesson.LessonNoteSiderbarDTO;
import vn.edu.fpt.entity.Lesson;
import vn.edu.fpt.entity.LessonNote;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.exception.ResourceNotFoundException;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.LessonNoteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonNoteService {

    private final LessonNoteRepository lessonNoteRepository;
    private final DtoMapper dtoMapper;
    private final vn.edu.fpt.repository.UserRepository userRepository;
    private final vn.edu.fpt.repository.LessonRepository lessonRepository;

    public List<LessonNoteSiderbarDTO> findLessonNoteByUserIdAndLessonId(Integer userId, Integer lessonId) {
        List<LessonNoteSiderbarDTO> lessonNotes = lessonNoteRepository.findByUser_IdAndLesson_IdOrderByCreatedAtDesc(userId, lessonId)
                .stream()
                .map(lessonNote -> {
                    return dtoMapper.toLessonNoteSiderbarDto(lessonNote);
                }).toList();
        return lessonNotes;
    }

    public LessonNote save(Integer userId, Integer lessonId, Integer noteId, Integer videoTimeSeconds, String noteContent) {
        if (noteContent != null && noteContent.trim().length() > 500) {
            throw new IllegalArgumentException("Nội dung ghi chú không được vượt quá 500 ký tự.");
        }

        LessonNote note;
        if (noteId != null) {
            note = lessonNoteRepository.findById(noteId)
                    .orElseThrow(() -> new vn.edu.fpt.exception.ResourceNotFoundException("Không tìm thấy ghi chú"));
            if (note.getUser() != null && !note.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("Bạn không có quyền sửa ghi chú này!");
            }
        } else {
            note = new LessonNote();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {return new IllegalIdentifierException("Không tìm thấy người dùng");
                });
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        note.setUser(user);
        note.setLesson(lesson);
        note.setVideoTimeSeconds(videoTimeSeconds);
        note.setNoteContent(noteContent);

        return lessonNoteRepository.save(note);
    }

    public void removeNote(User user, Integer lessonNoteId) {
        if (user == null) {
            throw new IllegalArgumentException("Bạn phải đăng nhập để thực hiện hành động này");
        }
        if (lessonNoteId == null) {
            throw new IllegalArgumentException("Không tìm thấy note id");
        }
        LessonNote lessonNote = lessonNoteRepository.findByUser_IdAndId(user.getId(), lessonNoteId);
        if (lessonNote == null) {
            throw new IllegalArgumentException("Bạn không có quyền xóa note với id " + lessonNoteId);
        }
        lessonNoteRepository.delete(lessonNote);
    }
}

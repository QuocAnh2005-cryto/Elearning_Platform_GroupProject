package vn.edu.fpt.controller;

import com.azure.core.annotation.Get;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.entity.LessonNote;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.service.UserService;
import vn.edu.fpt.service.lesson.LessonNoteService;
import vn.edu.fpt.util.SecurityUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lesson-note")
@RequiredArgsConstructor
public class LessonNoteRestController {

    private final LessonNoteService lessonNoteService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveNote(@RequestParam(value = "noteId", required = false) Integer noteId, @RequestParam("lessonId") Integer lessonId, @RequestParam("videoTimeSeconds") Integer videoTimeSeconds, @RequestParam("noteContent") String noteContent) {

        Map<String, Object> response = new HashMap<>();
        try {
            User currentUser = SecurityUtils.getCurrentUser();
            if (currentUser == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện hành động này!");
                return ResponseEntity.ok(response);
            }
            LessonNote savedNote = lessonNoteService.save(currentUser.getId(), lessonId, noteId, videoTimeSeconds, noteContent);
            response.put("success", true);
            response.put("noteId", savedNote.getId());
            response.put("message", "Lưu ghi chú thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/remove")
    public void removeLessonNote(@RequestParam("noteId") Integer lessonNoteId) {
        User user = SecurityUtils.getCurrentUser();
        lessonNoteService.removeNote(user, lessonNoteId);
    }
}

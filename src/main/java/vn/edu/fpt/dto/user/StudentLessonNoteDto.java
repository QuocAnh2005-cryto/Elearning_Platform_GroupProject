package vn.edu.fpt.dto.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLessonNoteDto {
    private Integer courseId;
    private String courseTitle;
    private List<NoteDetailDto> notes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NoteDetailDto {
        private Integer id;
        private Integer lessonId;
        private Integer sectionId;
        private String lessonTitle;
        private Integer videoTimeSeconds;
        private String formattedTime;
        private String noteContent;
        private LocalDateTime createdAt;
    }
}

package vn.edu.fpt.dto.user;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileDashboardDto {
    private UserDto currentUser;
    private int enrollmentsCount;
    private long certificatesCount;
    private int studyHours;
    private List<StudentLessonNoteDto> lessonNotes;
}

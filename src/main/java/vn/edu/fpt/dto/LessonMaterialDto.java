package vn.edu.fpt.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonMaterialDto {
    private Integer id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private LocalDateTime createdAt;

    private Integer courseId;
    private String courseTitle;
    private Integer lessonId;
    private String lessonTitle;
    private String sectionTitle;
    private String courseStatus;
    private String courseStatusLabel;
    private boolean deleteAllowed;
    private String deleteReason;

    public String getFormattedFileSize() {
        if (fileSize == null || fileSize == 0) {
            return "0 B";
        }
        if (fileSize >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
        if (fileSize >= 1024 * 1024) {
            return String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
        }
        if (fileSize >= 1024) {
            return String.format("%.2f KB", fileSize / 1024.0);
        }
        return fileSize + " Bytes";
    }
}

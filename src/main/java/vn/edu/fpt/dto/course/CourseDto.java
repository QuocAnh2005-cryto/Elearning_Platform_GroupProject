package vn.edu.fpt.dto.course;

import lombok.*;
import vn.edu.fpt.dto.*;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.util.AppConstants;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private Integer id;
    private UserDto instructor;
    private CategoryDto category;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String introVideoUrl;
    private BigDecimal price;
    private String level;
    private CourseStatus status;
    private Set<CourseSectionDto> sections;
    private Set<FeedbackDto> feedbacks;
    private String rejectionReason;

    private double averageRating;
    private int ratingCount;
    private int totalLessonsCount;
    private int enrollmentsCount;
    private String firstLessonVideoUrl;
    private Integer firstLessonId;
    private String thumbnailPath;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;

    public String getStatusLabel() {
        return status != null ? status.getLabel() : "";
    }

    public String getLevel() {
        if ("BEGINNER".equalsIgnoreCase(level)) {
            return "Cơ bản";
        } else if ("INTERMEDIATE".equalsIgnoreCase(level)) {
            return "Trung cấp";
        } else if ("ADVANCED".equalsIgnoreCase(level)) {
            return "Nâng cao";
        }
        return level;
    }

    // Đếm số lượng feedback đạt mức đánh giá sao tương ứng (star)
    public int getStarCount(int star) {
        if (feedbacks == null) {
            return 0;
        }
        int count = 0;
        // Duyệt qua tất cả feedbacks để lọc ra các đánh giá khớp số sao
        for (FeedbackDto fb : feedbacks) {
            if (fb.getRating() != null && fb.getRating() == star) {
                count++;
            }
        }
        return count;
    }

    // Tính tỷ lệ phần trăm của số đánh giá sao tương ứng so với tổng số đánh giá
    public double getStarPercentage(int star) {
        int total = ratingCount; // Tổng số lượt đánh giá
        if (total == 0) {
            return 0.0;
        }
        // Công thức: (Số lượng đánh giá X sao * 100) / Tổng số đánh giá
        double pct = (double) getStarCount(star) * 100.0 / total;
        
        // Làm tròn đến 1 chữ số sau phần thập phân (Ví dụ: 75.4)
        return Math.round(pct * 10.0) / 10.0;
    }

    public int getTotalDurationMinutes() {
        if (sections == null) {
            return 0;
        }
        int totalSeconds = 0;
        for (CourseSectionDto section : sections) {
            if (section.getLessons() != null) {
                for (LessonDto lesson : section.getLessons()) {
                    if (lesson.getDurationSeconds() != null) {
                        totalSeconds += lesson.getDurationSeconds();
                    }
                }
            }
        }
        return totalSeconds / 60;
    }

    public String getIntroVideoPath() {
        if (introVideoUrl == null || introVideoUrl.trim().isEmpty()) {
            return null;
        }
        if (introVideoUrl.startsWith("http://") || introVideoUrl.startsWith("https://")) {
            return introVideoUrl;
        }
        return AppConstants.AZURE_STORAGE_BASE_URL + "/" + AppConstants.AZURE_STORAGE_CONTAINER_VIDEOS + "/" + introVideoUrl;
    }
}

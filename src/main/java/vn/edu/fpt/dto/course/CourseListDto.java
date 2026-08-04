package vn.edu.fpt.dto.course;

import lombok.*;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.util.AppConstants;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseListDto {
    private Integer id;
    private String title;
    private String thumbnailUrl;
    private BigDecimal price;
    private String level;
    private String instructorFirstName;
    private String instructorLastName;
    private Integer instructorId;
    private Integer categoryId;
    private String categoryName;
    private Double averageRating;
    private Long ratingCount;
    private Long totalLessonsCount;
    private Long enrollmentsCount;
    private Long totalDurationSeconds;

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

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.instructor.lastName / firstName
    public UserDto getInstructor() {
        return UserDto.builder()
                .id(instructorId)
                .firstName(instructorFirstName)
                .lastName(instructorLastName)
                .build();
    }

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.category.id
    public CategoryDto getCategory() {
        if (categoryId == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(categoryId)
                .name(categoryName)
                .build();
    }

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.getTotalLessonsCount()
    public int getTotalLessonsCount() {
        return totalLessonsCount != null ? totalLessonsCount.intValue() : 0;
    }

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.averageRating
    public double getAverageRating() {
        return averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;
    }

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.ratingCount
    public int getRatingCount() {
        return ratingCount != null ? ratingCount.intValue() : 0;
    }

    // Helper method to keep compatibility with Thymeleaf template referring to
    // course.enrollmentsCount
    public int getEnrollmentsCount() {
        return enrollmentsCount != null ? enrollmentsCount.intValue() : 0;
    }

    public int getDurationMinutes() {
        return totalDurationSeconds != null ? (int)(totalDurationSeconds / 60) : 0;
    }

    // Resolved path for course thumbnail
    public String getThumbnailPath() {
        if (thumbnailUrl == null || thumbnailUrl.trim().isEmpty()) {
            return "/images/course_thumbnail.png";
        }
        if (thumbnailUrl.startsWith("http://") || thumbnailUrl.startsWith("https://")) {
            return thumbnailUrl;
        }

        return AppConstants.AZURE_STORAGE_BASE_URL + "/" +
                AppConstants.AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS + "/" +
                thumbnailUrl;
    }
}

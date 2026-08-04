package vn.edu.fpt.dto.home;

import lombok.*;
import vn.edu.fpt.dto.course.CategoryDto;
import vn.edu.fpt.dto.course.CourseListDto;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDto {
    private boolean hasFavorites;
    private CategoryDto parentCategory;
    private List<CategoryDto> favoriteChildren;
    private Map<Integer, List<CourseListDto>> coursesMap;

    // Các chỉ số thống kê từ cơ sở dữ liệu
    private Long totalCourses;
    private Long totalInstructors;
    private Long totalLearners;
    private Integer fiveStarRatingPercent;
}


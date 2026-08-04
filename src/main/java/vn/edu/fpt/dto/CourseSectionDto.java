package vn.edu.fpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSectionDto {
    private Integer id;

    @NotBlank(message = "Tên chương không được để trống")
    @Size(
            min = 3,
            max = 255,
            message = "Tên chương phải từ 3 đến 255 kí tự"
    )
    private String title;
    private Integer position;
    private CourseCreateDto course;
    private List<LessonDto> lessons;
}

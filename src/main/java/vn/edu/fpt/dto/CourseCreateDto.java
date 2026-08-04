package vn.edu.fpt.dto;

import com.azure.core.annotation.Get;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDto {

    private Integer id;

    @NotBlank(message = "Vui lòng nhập tiêu đề khóa học.")
    @Size(min = 10, max = 120, message = "Tiêu đề khóa học phải từ 10 đến 120 ký tự.")
    private String title;

    @NotBlank(message = "Vui lòng nhập mô tả khóa học.")
    @Size(min = 50, message = "Mô tả khóa học phải có tối thiểu 50 ký tự.")
    private String description;

    private MultipartFile thumbnailFile;

    @NotBlank(message = "Vui lòng chọn trình độ khóa học.")
    private String level;

    @NotNull(message = "Vui lòng nhập giá khóa học.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá khóa học không được âm.")
    private BigDecimal price;

    @NotNull(message = "Vui lòng chọn danh mục khóa học.")
    private Integer categoryId;

    private String thumbnailUrl;

    private String introVideoUrl;

    private String introVideoPreviewUrl;
}

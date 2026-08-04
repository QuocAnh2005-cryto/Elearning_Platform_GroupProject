package vn.edu.fpt.dto.transaction_manager;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CourseDTO {
    private Integer id;
    private String title;
    private BigDecimal price;
    private String thumbnailUrl;

    public CourseDTO(Integer id, String title, BigDecimal price, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
    }
}

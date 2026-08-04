package vn.edu.fpt.dto.cart;

import lombok.*;
import vn.edu.fpt.dto.course.CourseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Integer id;
    private CourseDto course;
    private boolean selected;
}

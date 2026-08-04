package vn.edu.fpt.dto.revenueInstructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRevenueDto {
    private String name;
    private BigDecimal revenue;
}

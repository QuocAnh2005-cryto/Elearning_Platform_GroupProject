package vn.edu.fpt.dto.revenueInstructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CoursePerformanceDto {

    private String name;
    private long salesCount;
    private int percent;

    public CoursePerformanceDto(String name, Long salesCount) {
        this.name = name;
        this.salesCount = salesCount;
    }
}

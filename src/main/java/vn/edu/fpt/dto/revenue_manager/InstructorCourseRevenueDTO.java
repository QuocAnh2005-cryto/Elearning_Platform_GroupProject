package vn.edu.fpt.dto.revenue_manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class InstructorCourseRevenueDTO {
    private Integer courseId;
    private String title;
    private BigDecimal price;
    private long salesCount;
    private BigDecimal totalRevenue;

    public InstructorCourseRevenueDTO(Integer courseId, String title, BigDecimal price, long salesCount, BigDecimal totalRevenue) {
        this.courseId = courseId;
        this.title = title;
        this.price = price;
        this.salesCount = salesCount;
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
    }

    public InstructorCourseRevenueDTO(Integer courseId, String title, BigDecimal price, Object salesCount, Object totalRevenue) {
        this.courseId = courseId;
        this.title = title;
        this.price = price;
        this.salesCount = salesCount instanceof Number ? ((Number) salesCount).longValue() : 0L;
        if (totalRevenue instanceof BigDecimal) {
            this.totalRevenue = (BigDecimal) totalRevenue;
        } else if (totalRevenue instanceof Number) {
            this.totalRevenue = BigDecimal.valueOf(((Number) totalRevenue).doubleValue());
        } else {
            this.totalRevenue = BigDecimal.ZERO;
        }
    }

    public BigDecimal getPlatformShare() {
        return totalRevenue == null ? BigDecimal.ZERO : totalRevenue.multiply(BigDecimal.valueOf(0.3));
    }

    public BigDecimal getInstructorShare() {
        return totalRevenue == null ? BigDecimal.ZERO : totalRevenue.multiply(BigDecimal.valueOf(0.7));
    }
}

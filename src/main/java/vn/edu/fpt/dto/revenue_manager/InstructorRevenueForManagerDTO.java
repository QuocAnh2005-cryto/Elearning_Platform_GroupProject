package vn.edu.fpt.dto.revenue_manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class InstructorRevenueForManagerDTO {
    private Integer instructorId;
    private String firstName;
    private String lastName;
    private String email;
    private long totalCourses;
    private long totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal platformShare;
    private BigDecimal instructorShare;

    public InstructorRevenueForManagerDTO(Integer instructorId, String firstName, String lastName, String email, 
                                           long totalCourses, long totalSales, BigDecimal totalRevenue) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalCourses = totalCourses;
        this.totalSales = totalSales;
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        this.platformShare = this.totalRevenue.multiply(BigDecimal.valueOf(0.3));
        this.instructorShare = this.totalRevenue.multiply(BigDecimal.valueOf(0.7));
    }

    public InstructorRevenueForManagerDTO(Integer instructorId, String firstName, String lastName, String email, 
                                           Object totalCourses, Object totalSales, Object totalRevenue) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalCourses = totalCourses instanceof Number ? ((Number) totalCourses).longValue() : 0L;
        this.totalSales = totalSales instanceof Number ? ((Number) totalSales).longValue() : 0L;
        if (totalRevenue instanceof BigDecimal) {
            this.totalRevenue = (BigDecimal) totalRevenue;
        } else if (totalRevenue instanceof Number) {
            this.totalRevenue = BigDecimal.valueOf(((Number) totalRevenue).doubleValue());
        } else {
            this.totalRevenue = BigDecimal.ZERO;
        }
        this.platformShare = this.totalRevenue.multiply(BigDecimal.valueOf(0.3));
        this.instructorShare = this.totalRevenue.multiply(BigDecimal.valueOf(0.7));
    }
}

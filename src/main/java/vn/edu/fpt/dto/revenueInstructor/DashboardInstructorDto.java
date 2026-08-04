package vn.edu.fpt.dto.revenueInstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardInstructorDto {
    private BigDecimal totalRevenue;

    private long totalOrders;

    private BigDecimal profit;

    private long totalStudents;

    private long totalCourse;
    private long newCoursesInPeriod;


    private List<String> revenueTrendLabels;
    private List<BigDecimal> revenueTrendValues;

    private List<CourseRevenueDto> topSellingCourses;
    private List<CoursePerformanceDto> coursePerformance;
    private List<RecentOrderDto> recentOrders;

    private String chartPoints;
}

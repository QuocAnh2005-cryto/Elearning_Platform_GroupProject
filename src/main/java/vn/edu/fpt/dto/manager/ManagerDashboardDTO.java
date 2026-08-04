package vn.edu.fpt.dto.manager;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ManagerDashboardDTO {
    private long totalInstructors;
    private long totalLearners;
    private long pendingCourses;
    private String totalRevenue;
    private List<String> chartLabels;
    private List<BigDecimal> chartData;

    public String getMonthlyRevenue() {
        return totalRevenue;
    }

    public void setMonthlyRevenue(String monthlyRevenue) {
        this.totalRevenue = monthlyRevenue;
    }
}

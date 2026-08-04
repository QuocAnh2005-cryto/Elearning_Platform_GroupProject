package vn.edu.fpt.dto.revenue_manager;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyRevenueForManagerDTO {
    private BigDecimal monthlyRevenue;
    private BigDecimal instructorRevenue;
    private BigDecimal platformRevenue;
    private Map<Integer, BigDecimal> revenueByPerWeek;

    public MonthlyRevenueForManagerDTO(BigDecimal monthlyRevenue)
             {
        this.monthlyRevenue = monthlyRevenue;
        this.instructorRevenue = BigDecimal.ZERO;
        this.platformRevenue = BigDecimal.ZERO;
        this.revenueByPerWeek = new HashMap<>();
    }

}

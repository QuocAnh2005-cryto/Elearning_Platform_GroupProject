package vn.edu.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRevenueDTO {
    private Integer year;
    private Integer month;
    private BigDecimal revenue;
}
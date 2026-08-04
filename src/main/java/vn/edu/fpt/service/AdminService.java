package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.repository.PaymentRepository;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.dto.MonthlyRevenueDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;

    public List<BigDecimal> getPlatformMonthlyRevenueChartData() {
        List<BigDecimal> chartData = new ArrayList<>();
        int currentYear = java.time.LocalDateTime.now().getYear();

        // Khởi tạo 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            chartData.add(BigDecimal.ZERO);
        }

        // Mốc đầu năm hiện tại (01/01/năm hiện tại 00:00:00)
        java.time.LocalDateTime startOfYear = java.time.LocalDateTime.now()
                .with(java.time.temporal.TemporalAdjusters.firstDayOfYear())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Lấy doanh thu thô từ Repository
        List<MonthlyRevenueDTO> rawChartData = paymentRepository.getMonthlyRevenue(PaymentStatus.PAID, startOfYear);

        for (MonthlyRevenueDTO row : rawChartData) {
            int year = row.getYear();
            int month = row.getMonth();
            BigDecimal amount = row.getRevenue();

            if (year == currentYear && month >= 1 && month <= 12) {
                // Doanh thu nền tảng chiếm 30% tổng số tiền thanh toán
                BigDecimal platformRevenue = amount.multiply(BigDecimal.valueOf(AppConstants.PLATFORM_FEE));
                chartData.set(month - 1, platformRevenue);
            }
        }

        return chartData;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalCourses() {
        return courseRepository.count();
    }

    public BigDecimal getRawPlatformRevenue() {
        BigDecimal totalPayments = paymentRepository.sumAmountByStatus(PaymentStatus.PAID);
        if (totalPayments == null) {
            return BigDecimal.ZERO;
        }
        return totalPayments.multiply(BigDecimal.valueOf(AppConstants.PLATFORM_FEE));
    }

    public String getPlatformRevenue() {
        BigDecimal platformRevenue = getRawPlatformRevenue();
        return formatRevenue(platformRevenue);
    }

    private String formatRevenue(BigDecimal revenue) {
        if (revenue == null) {
            return "0 đ";
        }
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols(java.util.Locale.of("vi", "VN"));
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(revenue) + " đ";
    }
}

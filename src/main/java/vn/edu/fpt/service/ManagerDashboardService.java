package vn.edu.fpt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import vn.edu.fpt.dto.manager.ManagerDashboardDTO;
import vn.edu.fpt.dto.MonthlyRevenueDTO;
import vn.edu.fpt.dto.revenue_manager.MonthlyRevenueForManagerDTO;
import vn.edu.fpt.dto.revenue_manager.InstructorRevenueForManagerDTO;
import vn.edu.fpt.dto.revenue_manager.InstructorCourseRevenueDTO;
import vn.edu.fpt.enums.CourseStatus;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.repository.PaymentRepository;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.util.AppConstants;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ManagerDashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;

    public ManagerDashboardService(
            UserRepository userRepository,
            CourseRepository courseRepository,
            PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.paymentRepository = paymentRepository;
    }

    public ManagerDashboardDTO getDashboardData() {
        ManagerDashboardDTO dto = new ManagerDashboardDTO();
        // Đếm tổng số lượng giảng viên
        long totalInstructors = userRepository.countInstructors();
        dto.setTotalInstructors(totalInstructors);
        // Đếm tổng số lượng học viên
        long totalLearners = userRepository.countLearners();
        dto.setTotalLearners(totalLearners);
        //Đếm số lượng khóa học đang ở trạng thái "PENDING"
        long pendingCourses = courseRepository.countByStatus(CourseStatus.PENDING);
        dto.setPendingCourses(pendingCourses);
        // Tính tổng doanh thu toàn hệ thống (status = PAID)
        BigDecimal totalRevenue = paymentRepository.sumAmountByStatus(PaymentStatus.PAID);
        dto.setTotalRevenue(formatRevenue(totalRevenue));
        // --- Chuẩn bị dữ liệu cho biểu đồ (12 tháng trong năm hiện tại) ---
        List<String> chartLabels = new ArrayList<>();// nhãn: "Tháng 1" -> "Tháng 12"
        List<BigDecimal> chartData = new ArrayList<>();// giá trị doanh thu tương ứng
        int currentYear = LocalDateTime.now().getYear();

        // Khởi tạo 12 nhãn từ Tháng 1 đến Tháng 12 và set mặc định giá trị = 0
        for (int i = 1; i <= 12; i++) {
            chartLabels.add("Tháng " + i);
            chartData.add(BigDecimal.ZERO);
        }

        // Xác định mốc đầu năm hiện tại (01/01/năm hiện tại 00:00:00)
        LocalDateTime startOfYear = LocalDateTime.now()
                .with(TemporalAdjusters.firstDayOfYear())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Lấy danh sách doanh thu theo tháng từ đầu năm hiện tại
        List<MonthlyRevenueDTO> rawChartData = paymentRepository.getMonthlyRevenue(PaymentStatus.PAID, startOfYear);

        // Duyệt dữ liệu thô và gán giá trị vào đúng vị trí trong chartData (chỉ cho năm hiện tại)
        for (MonthlyRevenueDTO row : rawChartData) {
            int year = row.getYear();
            int month = row.getMonth();
            BigDecimal amount = row.getRevenue();

            if (year == currentYear && month >= 1 && month <= 12) {
                chartData.set(month - 1, amount);
            }
        }

        dto.setChartLabels(chartLabels);
        dto.setChartData(chartData);

        return dto;
    }

    //hàm format tiền
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

    public MonthlyRevenueForManagerDTO getMonthlyRevenueForManager() {
        LocalDate today = LocalDate.now();
        LocalDate startDateOfMonth = today.withDayOfMonth(1);
        MonthlyRevenueForManagerDTO monthlyRevenueForManagerDTO = paymentRepository.getMonthlyRevenueTotal(startDateOfMonth, today);
        if (monthlyRevenueForManagerDTO == null) {
            return new MonthlyRevenueForManagerDTO(BigDecimal.ZERO);
        }
        if (monthlyRevenueForManagerDTO.getMonthlyRevenue() == null) {
            monthlyRevenueForManagerDTO.setMonthlyRevenue(BigDecimal.ZERO);
        }

        BigDecimal totalRevenue = monthlyRevenueForManagerDTO.getMonthlyRevenue();
        BigDecimal totalRevenuePlatform = totalRevenue.multiply(BigDecimal.valueOf(AppConstants.PLATFORM_FEE));
        BigDecimal totalRevenueInstructor = totalRevenue.subtract(totalRevenuePlatform);
        monthlyRevenueForManagerDTO.setInstructorRevenue(totalRevenueInstructor);
        monthlyRevenueForManagerDTO.setPlatformRevenue(totalRevenuePlatform);

        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            // nếu lấy được doanh thu tháng này và phần doanh thu tháng này lớn hơn 0 thì có thể tính doanh thu theo từng tuần của tháng này, tính từ đầu tháng.

            // tính tổng doanh thu của tất cả giảng viên
            Map<Integer, BigDecimal> revenueByPerWeek = monthlyRevenueForManagerDTO.getRevenueByPerWeek();
            if (revenueByPerWeek == null) {
                revenueByPerWeek = new HashMap<>();
            }
            // tính số tuần trong tháng, tính từ ngày hiện tại
            int weekOfMonth = (today.getDayOfMonth() - 1) / 7 + 1;

            for (int i = 1; i <= weekOfMonth; i++) {
                // ngày bắt đầu của tuần
                int startDay = (i - 1) * 7 + 1;
                LocalDate startDate = startDateOfMonth.withDayOfMonth(startDay);
                // ngày kết thycs của tuần
                LocalDate endDate;
                if (startDate.plusDays(6).isAfter(today)) {
                    endDate = today;
                } else {
                    endDate = startDate.plusDays(6);
                }
                MonthlyRevenueForManagerDTO revenueByWeek = paymentRepository.getMonthlyRevenueTotal(startDate, endDate);
                revenueByPerWeek.put(i, revenueByWeek == null ? BigDecimal.ZERO : revenueByWeek.getMonthlyRevenue());
            }
            monthlyRevenueForManagerDTO.setRevenueByPerWeek(revenueByPerWeek);
        }
        return monthlyRevenueForManagerDTO;
    }

    public Double getGrowthRate(BigDecimal monthlyRevenue) {
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }
        LocalDate startDateOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDateOfLastMonth = startDateOfLastMonth.withDayOfMonth(startDateOfLastMonth.lengthOfMonth());
        MonthlyRevenueForManagerDTO revenueLastMonthDto = paymentRepository.getMonthlyRevenueTotal(startDateOfLastMonth, endDateOfLastMonth);
        BigDecimal revenueLastMonth = revenueLastMonthDto == null ? BigDecimal.ZERO : revenueLastMonthDto.getMonthlyRevenue();
        if  (revenueLastMonth == null || revenueLastMonth.compareTo(BigDecimal.ZERO) <= 0) {
            if (monthlyRevenue.compareTo(BigDecimal.ZERO) == 0) {
                return 0D;
            }
            return 100D;
        }
        // tốc độ tăng trưởng là = ((doanh thu tháng này - doanh thu tháng trước) / daoanh thu tháng trước) * 100
        return ((monthlyRevenue.subtract(revenueLastMonth)).divide(revenueLastMonth, 2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    public Page<InstructorRevenueForManagerDTO> getInstructorsRevenue(String keyword, Integer month, Integer year, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return userRepository.getInstructorsRevenueStats(keyword, month, year, pageable);
    }

    public List<InstructorCourseRevenueDTO> getInstructorCourseRevenueDetails(Integer instructorId, Integer month, Integer year) {
        return courseRepository.getCourseRevenueStatsByInstructor(instructorId, month, year);
    }

    public User getInstructorById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên với ID: " + id));
    }
}

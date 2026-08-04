package vn.edu.fpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.fpt.dto.revenueInstructor.CoursePerformanceDto;
import vn.edu.fpt.dto.revenueInstructor.DashboardInstructorDto;
import vn.edu.fpt.dto.revenueInstructor.RecentOrderDto;
import vn.edu.fpt.repository.CourseRepository;
import vn.edu.fpt.repository.EnrollmentRepository;
import vn.edu.fpt.repository.OrderItemRepository;
import vn.edu.fpt.util.AppConstants;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardInstructorService {
    private final CourseRepository courseRepository;
    private final OrderItemRepository orderItemRepository;
    private final EnrollmentRepository enrollmentRepository;

    public DashboardInstructorDto getStats(Integer instructorId, String period, Integer year, Integer month) {

        LocalDateTime[] current = resolveDateRange(period, year, month);

        DashboardInstructorDto dto = new DashboardInstructorDto();


        BigDecimal curRevenue = orDefault(orderItemRepository.sumTotalRevenueByInstructor(instructorId, current[0], current[1]));
        dto.setTotalRevenue(curRevenue);
        dto.setProfit(curRevenue.multiply(BigDecimal.valueOf(1-AppConstants.PLATFORM_FEE)));


        long curOrders = orDefaultLong(orderItemRepository.countOrder(instructorId, current[0], current[1]));
        dto.setTotalOrders(curOrders);

        long curStudents = enrollmentRepository.countDistictStudents(instructorId, current[0], current[1]);
        dto.setTotalStudents(curStudents);


        long totalCourse = courseRepository.countPublishedCourse(instructorId, current[0], current[1]);
        long newCourses = courseRepository.countNewCourse(instructorId, current[0], current[1]);
        dto.setTotalCourse(totalCourse);
        dto.setNewCoursesInPeriod(newCourses);


        dto.setTopSellingCourses(
                orderItemRepository.topSellingCourse(instructorId, current[0], current[1], PageRequest.of(0, 4))
        );

        List<CoursePerformanceDto> perf =
                orderItemRepository.coursePerformance(instructorId, current[0], current[1], PageRequest.of(0, 4));
        long maxSales = perf.stream().mapToLong(CoursePerformanceDto::getSalesCount).max().orElse(1);
        perf.forEach(p -> p.setPercent(maxSales == 0 ? 0 : (int) (p.getSalesCount() * 100 / maxSales)));
        dto.setCoursePerformance(perf);

        dto.setRecentOrders(orderItemRepository.recentOrder(instructorId, PageRequest.of(0, 5)));

        List<Object[]> raw = orderItemRepository.revenueTrend(instructorId, current[0], current[1]);
        TrendSeries trendSeries = buildRevenueTrendSeries(period, current, raw);
        dto.setRevenueTrendLabels(trendSeries.labels());
        dto.setRevenueTrendValues(trendSeries.values());
        dto.setChartPoints(buildChartPoints(trendSeries.values()));

        return dto;
    }


    private long orDefaultLong(Long value) {
        return value != null ? value : 0L;
    }

    private BigDecimal orDefault(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private TrendSeries buildRevenueTrendSeries(String period, LocalDateTime[] current, List<Object[]> raw) {
        Map<LocalDate, BigDecimal> revenueByDate = new LinkedHashMap<>();
        for (Object[] row : raw) {
            LocalDate date = toLocalDate(row[0]);
            BigDecimal revenue = row[1] instanceof BigDecimal value ? value : BigDecimal.ZERO;
            revenueByDate.merge(date, revenue, BigDecimal::add);
        }

        if ("MONTH".equals(period) || "CUSTOM".equals(period)) {
            return buildWeeklySeries(current, revenueByDate);
        }
        if ("YEAR".equals(period)) {
            return buildMonthlySeries(current[0].getYear(), revenueByDate);
        }
        return buildRawSeries(revenueByDate);
    }

    private TrendSeries buildWeeklySeries(LocalDateTime[] current, Map<LocalDate, BigDecimal> revenueByDate) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        LocalDate start = current[0].toLocalDate();
        LocalDate end = current[1].toLocalDate();
        int year = start.getYear();
        int month = start.getMonthValue();

        int[][] weekRanges = {
            {1, 7},
            {8, 14},
            {15, 21},
            {22, 28},
            {29, end.getDayOfMonth()}
        };

        for (int i = 0; i < weekRanges.length; i++) {
            int startDay = weekRanges[i][0];
            int endDay = weekRanges[i][1];
            if (startDay > end.getDayOfMonth()) {
                break;
            }
            BigDecimal weekRevenue = BigDecimal.ZERO;
            for (int day = startDay; day <= endDay; day++) {
                LocalDate date = LocalDate.of(year, month, day);
                weekRevenue = weekRevenue.add(revenueByDate.getOrDefault(date, BigDecimal.ZERO));
            }
            labels.add("Tuần " + (i + 1) + " (" + startDay + "/" + month + " - " + endDay + "/" + month + ")");
            values.add(weekRevenue);
        }
        return new TrendSeries(labels, values);
    }

    private TrendSeries buildMonthlySeries(int year, Map<LocalDate, BigDecimal> revenueByDate) {
        Map<YearMonth, BigDecimal> revenueByMonth = new LinkedHashMap<>();
        revenueByDate.forEach((date, revenue) ->
                revenueByMonth.merge(YearMonth.from(date), revenue, BigDecimal::add)
        );

        List<String> labels = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            labels.add("T" + month);
            values.add(revenueByMonth.getOrDefault(yearMonth, BigDecimal.ZERO));
        }
        return new TrendSeries(labels, values);
    }

    private TrendSeries buildRawSeries(Map<LocalDate, BigDecimal> revenueByDate) {
        if (revenueByDate.isEmpty()) {
            return new TrendSeries(List.of(), List.of());
        }
        List<String> labels = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        revenueByDate.forEach((date, revenue) -> {
            labels.add(date.format(DateTimeFormatter.ofPattern("dd/MM")));
            values.add(revenue);
        });

        // Instructor dashboard chart: neu chi co 1 ngay co doanh thu thi them 2 moc 0 de SVG polyline nhin thay duoc bien dong.
        if (values.size() == 1) {
            labels.add(0, "");
            values.add(0, BigDecimal.ZERO);
            labels.add("");
            values.add(BigDecimal.ZERO);
        }
        return new TrendSeries(labels, values);
    }

    private LocalDate toLocalDate(Object dateObj) {
        if (dateObj instanceof LocalDate localDate) {
            return localDate;
        }
        if (dateObj instanceof Date date) {
            return date.toLocalDate();
        }
        if (dateObj instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        if (dateObj instanceof LocalDateTime localDateTime) {
            return localDateTime.toLocalDate();
        }
        throw new IllegalArgumentException("Unsupported date type: " + dateObj.getClass());
    }

    private String buildChartPoints(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) return "0,220 700,220";

        double max = values.stream().mapToDouble(BigDecimal::doubleValue).max().orElse(1);
        if (max == 0) max = 1;

        int width = 700, height = 220;
        int n = values.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            double x = n == 1 ? 0 : (double) width * i / (n - 1);
            double y = height - (values.get(i).doubleValue() / max * height);
            sb.append((int) x).append(",").append((int) y);
            if (i < n - 1) sb.append(" ");
        }
        return sb.toString();
    }

    private record TrendSeries(List<String> labels, List<BigDecimal> values) {
    }

    private LocalDateTime[] resolveDateRange(String period, Integer year, Integer month) {
        LocalDate now = LocalDate.now();
        return switch (period) {
            case "MONTH" -> new LocalDateTime[]{
                    now.withDayOfMonth(1).atStartOfDay(),
                    now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59)
            };

            case "YEAR" -> new LocalDateTime[]{
                    now.withDayOfYear(1).atStartOfDay(),
                    LocalDate.of(now.getYear(), 12, 31).atTime(23, 59, 59)
            };

            case "CUSTOM" -> {
                LocalDate d = LocalDate.of(year, month, 1);
                yield new LocalDateTime[]{
                        d.withDayOfMonth(1).atStartOfDay(),
                        d.withDayOfMonth(d.lengthOfMonth()).atTime(23, 59, 59)
                };
            }

            default -> new LocalDateTime[]{
                    LocalDateTime.of(2000, 1, 1, 0, 0),
                    LocalDateTime.now()
            };
        };
    }

}

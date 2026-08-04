package vn.edu.fpt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.dto.manager.ManagerDashboardDTO;
import vn.edu.fpt.dto.revenue_manager.MonthlyRevenueForManagerDTO;
import vn.edu.fpt.service.ManagerDashboardService;
import vn.edu.fpt.util.AppConstants;

import java.time.LocalDate;
import java.util.List;
import vn.edu.fpt.dto.revenue_manager.InstructorRevenueForManagerDTO;
import vn.edu.fpt.dto.revenue_manager.InstructorCourseRevenueDTO;
import vn.edu.fpt.entity.User;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerDashboardController {

    private final ManagerDashboardService managerDashboardService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        ManagerDashboardDTO data = managerDashboardService.getDashboardData();

        model.addAttribute("totalInstructors", data.getTotalInstructors());
        model.addAttribute("totalLearners", data.getTotalLearners());
        model.addAttribute("pendingCourses", data.getPendingCourses());
        model.addAttribute("totalRevenue", data.getTotalRevenue());
        model.addAttribute("monthlyRevenue", data.getMonthlyRevenue());
        model.addAttribute("chartLabels", data.getChartLabels());
        model.addAttribute("chartData", data.getChartData());

        return "manager/dashboard/dashboard";
    }



    @GetMapping("/revenue/list")
    public String revenueList(Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MonthlyRevenueForManagerDTO monthlyRevenueForManagerDTO = managerDashboardService.getMonthlyRevenueForManager();
        Double growthRate = managerDashboardService.getGrowthRate(monthlyRevenueForManagerDTO.getMonthlyRevenue());
        LocalDate today = LocalDate.now();
        model.addAttribute("monthlyRevenue", monthlyRevenueForManagerDTO);
        model.addAttribute("growthRate", growthRate);
        model.addAttribute("today", today);
        model.addAttribute("weeklyRevenueJson", mapper.writeValueAsString(monthlyRevenueForManagerDTO.getRevenueByPerWeek()));
        return "manager/revenue/revenue-list";
    }

    @GetMapping("/revenue/instructor")
    public String instructorRevenueList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Integer queryMonth = month == 0 ? null : month;
        Integer queryYear = year == 0 ? null : year;

        Page<InstructorRevenueForManagerDTO> instructorRevenues =
                managerDashboardService.getInstructorsRevenue(keyword, queryMonth, queryYear, page);

        int startPage = 0;
        int endPage = 0;
        if (instructorRevenues.getTotalPages() > 0) {
            startPage = (instructorRevenues.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
            endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, instructorRevenues.getTotalPages() - 1);
        }

        model.addAttribute("instructorRevenues", instructorRevenues);
        model.addAttribute("keyword", keyword);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "manager/revenue/instructor-revenue";
    }

    @GetMapping("/revenue/instructor/details/{id}")
    public String instructorRevenueDetails(
            @PathVariable Integer id, 
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {
        
        Integer queryMonth = (month != null && month == 0) ? null : month;
        Integer queryYear = (year != null && year == 0) ? null : year;

        User instructor = managerDashboardService.getInstructorById(id);
        List<InstructorCourseRevenueDTO> courseDetails =
                managerDashboardService.getInstructorCourseRevenueDetails(id, queryMonth, queryYear);

        model.addAttribute("instructorName", instructor.getFirstName() + " " + instructor.getLastName());
        model.addAttribute("instructorEmail", instructor.getEmail());
        model.addAttribute("courseDetails", courseDetails);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        return "manager/revenue/instructor-revenue-detail";
    }
}

package vn.edu.fpt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.enums.UserStatus;
import vn.edu.fpt.service.AdminService;
import vn.edu.fpt.service.UserService;
import vn.edu.fpt.service.SystemLogService;
import vn.edu.fpt.entity.SystemLog;
import vn.edu.fpt.util.AppConstants;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final SystemLogService systemLogService;

    public AdminController(UserService userService, AdminService adminService, SystemLogService systemLogService) {
        this.userService = userService;
        this.adminService = adminService;
        this.systemLogService = systemLogService;
    }

    /**
     * GET /admin or GET /admin/dashboard
     * Hiển thị Dashboard tổng quan của Admin.
     */

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        long totalUsers = adminService.getTotalUsers();
        long totalCourses = adminService.getTotalCourses();
        String platformRevenue = adminService.getPlatformRevenue();

        List<String> chartLabels = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            chartLabels.add("Tháng " + i);
        }
        List<BigDecimal> chartData = adminService.getPlatformMonthlyRevenueChartData();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("platformRevenue", platformRevenue);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "admin/dashboard/dashboard";
    }

    /**
     * GET /admin/manager/list
     * Hiển thị danh sách Manager, hỗ trợ tìm kiếm và lọc.
     */
    @GetMapping("/manager/list")
    public String listManagers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UserDto> requestPage = userService.searchAndFilterManagers(keyword, status, pageable);

        int startPage = 0;
        int endPage = 0;
        if (requestPage.getTotalPages() > 0) {
            startPage = (requestPage.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * vn.edu.fpt.util.AppConstants.NUMBER_PAGE_PER_BLOCK;
            endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, requestPage.getTotalPages() - 1);
        }

        model.addAttribute("requestPage", requestPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/manager/manager-list";
    }

    /**
     * GET /admin/manager/detail/{id}
     * Hiển thị chi tiết thông tin một Manager.
     */
    @GetMapping("/manager/detail/{id}")
    public String detailManager(@PathVariable Integer id, Model model) {
        UserDto request = userService.getManagerDetail(id);
        List<SystemLog> logs = systemLogService.getLogsByUserId(id);
        model.addAttribute("request", request);
        model.addAttribute("logs", logs);
        return "admin/manager/manager-detail";
    }

    /**
     * POST /admin/manager/edit/{id}
     * Cập nhật trạng thái tài khoản Manager (ACTIVE / BANNED).
     */
    @PostMapping("/manager/edit/{id}")
    public String updateManagerStatus(
            @PathVariable Integer id,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateManagerStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản Manager thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/manager/detail/" + id;
    }
}

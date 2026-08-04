package vn.edu.fpt.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return (accept != null && accept.contains("application/json")) || request.getRequestURI().startsWith("/api/");
    }

    private ModelAndView buildErrorView(String viewName, HttpStatus status, String error, String message, HttpServletRequest request, Exception ex) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("status", status.value());
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.addObject("path", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());

        if (ex != null) {
            log.error("Lỗi tại {}: ", request.getRequestURI(), ex);
            log.error(error);
            log.error("Message lỗi {}", message);
        }
        return mav;
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public Object handleCourseNotFound(CourseNotFoundException ex, HttpServletRequest request) throws Exception {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Không tìm thấy khóa học", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        return buildErrorView("error/404", HttpStatus.NOT_FOUND, "Không tìm thấy khóa học", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Truy cập bị từ chối", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
        }

        return buildErrorView("error/403", HttpStatus.FORBIDDEN, "Truy cập bị từ chối", "Bạn không có quyền truy cập vào trang này. Vui lòng đăng nhập với tài khoản phù hợp.", request, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Không tìm thấy tài nguyên", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        return buildErrorView("error/404", HttpStatus.NOT_FOUND, "Không tìm thấy tài nguyên", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Yêu cầu không hợp lệ", ex.getMessage(), request.getRequestURI());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        return buildErrorView("error/400", HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("Không tìm thấy tài nguyên tĩnh tại [{}]: {}", request.getRequestURI(), ex.getMessage());
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Không tìm thấy trang", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        return buildErrorView("error/404", HttpStatus.NOT_FOUND, "Không tìm thấy trang", "Không tìm thấy trang yêu cầu hoặc trang đã bị xóa.", request, null);
    }

    /**
     * Bắt lỗi Race Condition (Optimistic Locking):
     * Xảy ra khi 2 Manager cùng duyệt/từ chối một khóa học đồng thời.
     * Hibernate phát hiện version đã thay đổi → ném exception này.
     * Redirect về trang trước với thông báo lỗi thân thiện thay vì hiện trang 500.
     */
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public Object handleOptimisticLockingFailure(
            org.springframework.orm.ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request) {

        log.warn("Optimistic locking conflict at [{}]: {}", request.getRequestURI(), ex.getMessage());

        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.CONFLICT.value(), "Xung đột dữ liệu",
                    "Dữ liệu vừa được cập nhật bởi người dùng khác. Vui lòng thử lại.", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        String referer = request.getHeader("Referer");
        org.springframework.web.servlet.support.RequestContextUtils.getOutputFlashMap(request)
                .put("errorMessage", "Dữ liệu khóa học vừa được cập nhật hoặc phê duyệt bởi quản lý khác.");
        return new ModelAndView("redirect:" + (referer != null && !referer.isBlank() ? referer : "/manager/course/list"));
    }

    /**
     * Bắt lỗi vi phạm nghiệp vụ (Business Rule Violation):
     * Ví dụ: Manager cố duyệt khóa học đã được xử lý trước đó (status != PENDING).
     * Redirect về trang trước với thông báo lỗi thay vì hiện trang 500.
     */
    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.warn("Illegal state at [{}]: {}", request.getRequestURI(), ex.getMessage());

        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.CONFLICT.value(), "Trạng thái không hợp lệ",
                    ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        String referer = request.getHeader("Referer");
        org.springframework.web.servlet.support.RequestContextUtils.getOutputFlashMap(request)
                .put("errorMessage", ex.getMessage());
        return new ModelAndView("redirect:" + (referer != null && !referer.isBlank() ? referer : "/manager/course/list"));
    }

    @ExceptionHandler(Exception.class)
    public Object handleOther(Exception ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi máy chủ nội bộ", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return buildErrorView("error/500", HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra sự cố hệ thống", "Máy chủ gặp sự cố bất ngờ khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.", request, ex);
    }

    @ExceptionHandler(PaymentCallApiException.class)
    public Object handlePaymetApiCallFailException(PaymentCallApiException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.BAD_GATEWAY.value(), "Lỗi kết nối cổng thanh toán", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.BAD_GATEWAY);
        }
        return buildErrorView("error/500", HttpStatus.BAD_GATEWAY, "Lỗi kết nối thanh toán", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(PaymentCreateException.class)
    public Object handlePaymentCreateException(PaymentCreateException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse body = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tạo giao dịch thanh toán thất bại", ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return buildErrorView("error/500", HttpStatus.INTERNAL_SERVER_ERROR, "Tạo thanh toán thất bại", ex.getMessage(), request, ex);
    }

}

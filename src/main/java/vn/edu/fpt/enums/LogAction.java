package vn.edu.fpt.enums;

import lombok.Getter;

@Getter
public enum LogAction {
    // Authentication & Security
    LOGIN_SUCCESS("Đăng nhập thành công"),
    LOGIN_FAILED("Đăng nhập thất bại"),
    LOGOUT("Đăng xuất"),
    CHANGE_PASSWORD("Đổi mật khẩu"),
    RESET_PASSWORD_REQUEST("Yêu cầu khôi phục mật khẩu"),

    // Payment & Transactions
    CREATE_PAYMENT("Tạo yêu cầu thanh toán"),
    PAYMENT_COMPLETED("Thanh toán thành công"),
    CANCEL_PAYMENT("Hủy thanh toán"),
    EXPIRE_PAYMENT("Thanh toán hết hạn"),

    // Approvals
    APPROVE_INSTRUCTOR("Phê duyệt giảng viên"),
    REJECT_INSTRUCTOR("Từ chối giảng viên"),
    APPROVE_COURSE("Phê duyệt khóa học"),
    REJECT_COURSE("Từ chối khóa học"),
    RESUBMIT_COURSE("Gửi lại duyệt khóa học"),
    MANUAL_ENROLLMENT_GRANTED("Quản lí cấp quyền xem khóa học"),

    // Course Management
    CREATE_COURSE("Tạo khóa học"),
    UPDATE_COURSE_PRICE("Cập nhật giá khóa học"),
    DELETE_COURSE("Xóa khóa học"),
    CREATE_LESSON("Tạo bài học"),
    DELETE_LESSON("Xóa bài học"),

    // Admin Operations
    BLOCK_USER("Khóa tài khoản"),
    UNBLOCK_USER("Mở khóa tài khoản");

    private final String label;

    LogAction(String label) {
        this.label = label;
    }
}

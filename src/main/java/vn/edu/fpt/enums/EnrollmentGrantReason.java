package vn.edu.fpt.enums;

import lombok.Getter;

@Getter
public enum EnrollmentGrantReason {

    PAYMENT_RECOVERY("Khôi phục sau thanh toán"),

    GIFT("Tặng khóa học"),

    OTHER("Khác");

    private final String label;

    EnrollmentGrantReason(String label) {
        this.label = label;
    }
}

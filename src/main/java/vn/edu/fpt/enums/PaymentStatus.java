package vn.edu.fpt.enums;

public enum PaymentStatus {
    /**
     * Payment link created, waiting for payment
     */
    PENDING("Chờ thanh toán"),

    /**
     * Payment successful
     */
    PAID("Đã thanh toán"),

    /**
     * Payment failed
     */
    FAILED("Thất bại"),

    /**
     * Payment link expired
     */
    EXPIRED("Hết hạn"),

    /**
     * Payment cancelled by user or system
     */
    CANCELLED("Đã hủy");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

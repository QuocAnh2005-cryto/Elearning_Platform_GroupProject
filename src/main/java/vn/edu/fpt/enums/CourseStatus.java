package vn.edu.fpt.enums;

public enum CourseStatus {
    DRAFT("Bản nháp"),
    PENDING("Đang chờ duyệt"),
    PUBLISHED("Đã duyệt"),
    REJECTED("Từ chối"),
    RESUBMIT("Duyệt lại"),
    HIDDEN("Ẩn");

    private final String label;

    CourseStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

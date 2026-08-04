package vn.edu.fpt.enums;

import lombok.Getter;

@Getter
public enum FeedbackReportReason {
    SPAM("Spam / Quảng cáo"),
    OFFENSIVE_LANGUAGE("Ngôn từ thô tục"),
    HARASSMENT("Quấy rối / Công kích"),
    FAKE_REVIEW("Đánh giá giả mạo"),
    OTHER("Lý do khác");

    private final String displayName;

    FeedbackReportReason(String displayName) {
        this.displayName = displayName;
    }
}

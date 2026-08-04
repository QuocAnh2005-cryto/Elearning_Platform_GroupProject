package vn.edu.fpt.enums;

import lombok.Getter;

@Getter
public enum LessonReportReason {
    CONTENT_IMPROVEMENT("Cải thiện nội dung"),
    VIDEO_ISSUE("Lỗi video"),
    AUDIO_ISSUE("Lỗi âm thanh"),
    OFFENSIVE_CONTENT("Nội dung phản cảm"),
    SUBTITLE_ISSUE("Lỗi phụ đề"),
    GENERAL("Vấn đề chung"),
    PROBLEM("Gặp sự cố");

    private final String displayName;

    LessonReportReason(String displayName) {
        this.displayName = displayName;
    }
}

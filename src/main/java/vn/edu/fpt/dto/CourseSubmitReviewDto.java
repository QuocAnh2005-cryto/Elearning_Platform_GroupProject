package vn.edu.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.enums.CourseStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubmitReviewDto {
    private Integer courseId;
    private String courseTitle;
    private CourseStatus courseStatus;
    private int completedCount;
    private int totalCount;
    private int percent;
    private boolean submitReady;
    private List<CheckItem> contentChecks = new ArrayList<>();
    private List<CheckItem> businessChecks = new ArrayList<>();
    private List<String> missingMessages = new ArrayList<>();

    public boolean isReadyExceptPolicy() {
        return contentChecks.stream().allMatch(CheckItem::isPassed)
                && businessChecks.stream()
                .filter(item -> !item.isPolicy())
                .allMatch(CheckItem::isPassed);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckItem {
        private String label;
        private boolean passed;
        private boolean policy;

        public CheckItem(String label, boolean passed) {
            this(label, passed, false);
        }
    }
}

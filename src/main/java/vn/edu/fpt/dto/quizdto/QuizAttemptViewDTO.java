package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptViewDTO {

    private Integer id;
    private BigDecimal score;
    private boolean passed;
    private LocalDateTime submittedAt;
    private int attemptNumber;
    private boolean latest;
    private boolean active;

    public String getStatusLabel() {
        return passed ? "Đạt" : "Chưa đạt";
    }

    public String getShortStatusLabel() {
        return passed ? "ĐẠT" : "KHÔNG ĐẠT";
    }

    public String getTitle() {
        return "Lần làm bài thứ " + attemptNumber + (latest ? " (Gần nhất)" : "");
    }
}

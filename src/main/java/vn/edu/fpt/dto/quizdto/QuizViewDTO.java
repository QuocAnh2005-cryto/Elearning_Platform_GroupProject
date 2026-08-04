package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizViewDTO {

    private Integer id;
    private String title;
    private Integer passScorePercent;
    private Integer timeLimitMinutes;
    private boolean active;
    private boolean showingResult;
    private QuizAttemptViewDTO selectedAttempt;

    @Builder.Default
    private List<QuizQuestionViewDTO> questions = new ArrayList<>();

    @Builder.Default
    private List<QuizAttemptViewDTO> attempts = new ArrayList<>();

    public int getQuestionCount() {
        return questions != null ? questions.size() : 0;
    }

    public Integer getTotalSore() {
        Integer totalScore = Integer.valueOf(0);
        for (QuizQuestionViewDTO attem : questions) {
            totalScore += attem.getPoints() == null ? 1 : attem.getPoints();
        }
        return totalScore;
    }

    public boolean hasAttempts() {
        return attempts != null && !attempts.isEmpty();
    }

    public QuizAttemptViewDTO getLatestAttempt() {
        return hasAttempts() ? attempts.get(0) : null;
    }

    public int getOlderAttemptCount() {
        return hasAttempts() ? Math.max(attempts.size() - 1, 0) : 0;
    }
}

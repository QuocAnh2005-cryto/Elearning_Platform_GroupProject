package vn.edu.fpt.dto.quizdto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDTO {

    private Integer id;

    private String title;

    private String description;

    private Integer passScorePercent;

    private String status;

    private Integer timeLimitMinutes;

    private Boolean isRandomQuestion;

    private Boolean isRandomAnswer;

    private LocalDateTime publishedAt;

    private Integer lessonId;

    @Builder.Default
    private List<QuizQuestionDTO> questions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Integer getQuestionCount() {
        return questions != null ? questions.size() : 0;
    }

    public Integer getTotalPoints() {
        if (questions == null || questions.isEmpty()) {
            return 0;
        }

        int totalPoints = 0;

        for (QuizQuestionDTO question : questions) {
            if (question == null || question.getPoints() == null) {
                continue;
            }
            totalPoints += question.getPoints();
        }

        return totalPoints;
    }
}
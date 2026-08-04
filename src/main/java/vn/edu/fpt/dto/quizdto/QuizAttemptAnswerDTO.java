package vn.edu.fpt.dto.quizdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptAnswerDTO {
    private Integer id;
    private Integer attemptId;
    private Integer questionId;
    private Integer selectedAnswerId;
    private Boolean isCorrect;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

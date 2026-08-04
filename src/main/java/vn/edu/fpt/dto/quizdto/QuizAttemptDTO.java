package vn.edu.fpt.dto.quizdto;

import lombok.*;
import vn.edu.fpt.dto.user.UserDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptDTO {
    private UserDto user;
    private BigDecimal score;
    private Boolean passed;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuizAttemptAnswerDTO> attemptAnswers;
}

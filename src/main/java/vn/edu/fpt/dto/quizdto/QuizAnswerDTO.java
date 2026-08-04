package vn.edu.fpt.dto.quizdto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerDTO {

    private String answerText;
    private Boolean correct;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

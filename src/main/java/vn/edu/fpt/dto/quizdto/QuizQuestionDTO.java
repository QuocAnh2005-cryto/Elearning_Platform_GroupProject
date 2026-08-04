package vn.edu.fpt.dto.quizdto;

import lombok.*;
import vn.edu.fpt.enums.QuestionType;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestionDTO {
    private String questionText;


    @Builder.Default
    private String questionType = QuestionType.SINGLE.name();
    @Builder.Default
    private Integer points = 1;
    private Integer position;

    @Builder.Default
    private List<QuizAnswerDTO> answers = new ArrayList<>();

    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String explanation;

}

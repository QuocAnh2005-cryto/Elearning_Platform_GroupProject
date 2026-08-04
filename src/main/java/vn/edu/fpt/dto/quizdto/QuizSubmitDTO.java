package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmitDTO {

    private Integer quizId;

    @Builder.Default
    private List<QuizQuestionSubmitDTO> questions = new ArrayList<>();
}

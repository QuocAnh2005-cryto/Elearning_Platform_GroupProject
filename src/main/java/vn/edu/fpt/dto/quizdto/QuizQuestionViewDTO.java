package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestionViewDTO {

    private Integer id;
    private String questionText;
    private String questionType;
    private Integer points;
    private Integer position;
    private String explanation;
    private int formIndex;

    @Builder.Default
    private List<QuizAnswerViewDTO> answers = new ArrayList<>();

    public boolean isMultipleChoice() {
        return QuestionType.MULTIPLE.name().equals(questionType);
    }

    public int getDisplayPoints() {
        return points != null ? points : 1;
    }
}

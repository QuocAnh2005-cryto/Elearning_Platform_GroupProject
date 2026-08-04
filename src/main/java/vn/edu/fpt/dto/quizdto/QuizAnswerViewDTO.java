package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerViewDTO {

    private Integer id;
    private String answerText;
    private Integer position;
    private boolean correct;
    private boolean selected;

    public String getReviewClass() {
        if (selected && correct) {
            return "is-user-correct";
        }
        if (selected) {
            return "is-user-wrong";
        }
        if (correct) {
            return "is-correct-missed";
        }
        return "";
    }

    public String getReviewLabel() {
        if (selected && correct) {
            return "Lựa chọn của bạn (Đúng)";
        }
        if (selected) {
            return "Lựa chọn của bạn (Sai)";
        }
        if (correct) {
            return "Đáp án đúng";
        }
        return "";
    }
}

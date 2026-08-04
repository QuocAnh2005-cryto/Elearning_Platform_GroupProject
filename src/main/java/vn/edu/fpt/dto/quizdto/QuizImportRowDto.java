package vn.edu.fpt.dto.quizdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizImportRowDto {

    private Integer row;

    private String question;

    private String type;

    private Integer points;

    private List<String> answers;

    private String correct;

    private String explanation;

}

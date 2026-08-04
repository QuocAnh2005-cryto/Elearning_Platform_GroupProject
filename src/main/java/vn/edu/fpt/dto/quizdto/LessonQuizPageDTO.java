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
public class LessonQuizPageDTO {

    private Integer lessonId;
    private String lessonTitle;
    private Integer activeQuizId;
    private boolean retake;
    private boolean previewMode;
    private QuizViewDTO activeQuiz;

    @Builder.Default
    private List<QuizViewDTO> quizzes = new ArrayList<>();

    public int getQuizCount() {
        return quizzes != null ? quizzes.size() : 0;
    }

    public int getTotalQuestions() {
        if (quizzes == null) {
            return 0;
        }

        int total = 0;
        for (QuizViewDTO quiz : quizzes) {
            total += quiz.getQuestionCount();
        }
        return total;
    }

    public boolean isEmpty() {
        return quizzes == null || quizzes.isEmpty();
    }
}

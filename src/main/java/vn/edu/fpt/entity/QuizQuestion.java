package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String questionText;

    @Column(name = "question_type", length = 20)
    private String questionType;

    @Column
    private Integer points;

    @Column
    private Integer position;

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAnswer> answers = new ArrayList<>();

    @Column(name = "explanation",
            columnDefinition = "NVARCHAR(MAX)")
    private String explanation;

    public void addAnswer(QuizAnswer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

    public void removeAnswer(QuizAnswer answer) {
        answers.remove(answer);
        answer.setQuestion(null);
    }
}


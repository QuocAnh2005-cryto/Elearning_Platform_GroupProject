package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "quiz_answers")
public class QuizAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @Column(name = "answer_text", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String answerText;

    @Builder.Default
    @Column(name = "is_correct")
    private Boolean correct = false;

    @Column(name = "position")
    private Integer position;
}


package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
@Table(name = "quizzes")
public class Quiz extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "pass_score_percent", nullable = false)
    private Integer passScorePercent;

    @Builder.Default
    @Column(length = 20, nullable = false)
    private String status = "DRAFT";

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Builder.Default
    @Column(name = "is_random_question", nullable = false)
    private Boolean isRandomQuestion = false;

    @Builder.Default
    @Column(name = "is_random_answer", nullable = false)
    private Boolean isRandomAnswer = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Builder.Default
    @OneToMany(
            mappedBy = "quiz",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuizQuestion> questions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "quiz",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<QuizAttempt> attempts = new HashSet<>();

    public void addQuestion(QuizQuestion question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(QuizQuestion question) {
        questions.remove(question);
        question.setQuiz(null);
    }

    public void addAttempt(QuizAttempt attempt) {
        attempts.add(attempt);
        attempt.setQuiz(this);
    }

    public void removeAttempt(QuizAttempt attempt) {
        attempts.remove(attempt);
        attempt.setQuiz(null);
    }



}


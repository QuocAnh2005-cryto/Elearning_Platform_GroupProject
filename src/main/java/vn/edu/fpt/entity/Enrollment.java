package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(name = "UQ_enrollment", columnNames = {"user_id", "course_id"}))
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "progress_percent", precision = 5, scale = 2)
    private BigDecimal progressPercent;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder.Default
    @OneToMany(mappedBy = "enrollment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonProgress> lessonProgresses = new HashSet<>();

    public void addLessonProgress(LessonProgress progress) {
        lessonProgresses.add(progress);
        progress.setEnrollment(this);
    }

    public void removeLessonProgress(LessonProgress progress) {
        lessonProgresses.remove(progress);
        progress.setEnrollment(null);
    }
}


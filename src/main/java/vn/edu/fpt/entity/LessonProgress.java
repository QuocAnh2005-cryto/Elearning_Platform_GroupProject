package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "lesson_progress",
        uniqueConstraints = @UniqueConstraint(name = "UQ_lesson_progress", columnNames = {"enrollment_id", "lesson_id"}))
public class LessonProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Builder.Default
    @Column(name = "is_completed")
    private Boolean completed = false;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
}


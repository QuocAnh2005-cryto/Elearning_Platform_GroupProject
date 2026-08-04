package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection courseSection;

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "video_url", columnDefinition = "NVARCHAR(500)")
    private String videoUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    private Integer position;

    @Builder.Default
    @Column(name = "is_published")
    private Boolean published = false;

    @Column(name = "moderation_status", length = 20)
    private String moderationStatus;

    @Builder.Default
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonMaterial> materials = new HashSet<>();

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        quiz.setLesson(this);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
        quiz.setLesson(null);
    }

    public void addMaterial(LessonMaterial material) {
        materials.add(material);
        material.setLesson(this);
    }

    public void removeMaterial(LessonMaterial material) {
        materials.remove(material);
        material.setLesson(null);
    }
}


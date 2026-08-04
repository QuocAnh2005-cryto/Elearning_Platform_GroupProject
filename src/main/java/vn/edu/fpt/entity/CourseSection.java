package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "course_sections")
public class CourseSection extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer position;

    @Builder.Default
    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position asc")
    private Set<Lesson> lessons = new LinkedHashSet<>();

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourseSection(this);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setCourseSection(null);
    }
}


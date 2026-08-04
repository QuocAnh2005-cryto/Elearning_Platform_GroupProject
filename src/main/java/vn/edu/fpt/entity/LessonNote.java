package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "lesson_notes")
public class LessonNote extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id",   nullable = false)
    private Lesson lesson;

    @Column(name = "video_timestamp_seconds",  nullable = false, columnDefinition = "INT")
    private Integer videoTimeSeconds;

    @Column(name = "note_content", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noteContent;

}

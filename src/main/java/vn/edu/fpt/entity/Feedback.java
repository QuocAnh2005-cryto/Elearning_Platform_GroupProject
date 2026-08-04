package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import vn.edu.fpt.enums.FeedbackStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "feedbacks")
public class Feedback extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column
    private Integer rating;

    
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FeedbackStatus status;

}


package vn.edu.fpt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;


    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> children = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    @Column(length = 20)
    private String status;

    public void addCourse(Course course) {
        courses.add(course);
        course.setCategory(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setCategory(null);
    }
}

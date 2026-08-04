package vn.edu.fpt.dto.instructor;

import lombok.*;
import vn.edu.fpt.dto.course.CourseListDto;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorPublicProfileDto {
    private Integer id;
    private String fullName;
    private String avatarUrl;
    private String email;
    private String phone;
    private String bio;
    
    private double averageRating;
    private int totalReviews;
    private int totalStudents;
    private int totalCourses;
    
    private int percent5Stars;
    private int percent4Stars;
    private int percent3Stars;
    private int percent2Stars;
    private int percent1Stars;
    private List<CourseListDto> courses;
}

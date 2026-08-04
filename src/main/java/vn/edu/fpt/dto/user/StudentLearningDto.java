package vn.edu.fpt.dto.user;

import lombok.*;
import vn.edu.fpt.dto.EnrollmentDto;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLearningDto {
    private UserDto currentUser;
    private List<EnrollmentDto> enrollments;
    private int enrollmentsCount;
    private String filter;
    private int currentPage;
    private int totalPages;
}

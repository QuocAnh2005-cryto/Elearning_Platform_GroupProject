package vn.edu.fpt.dto;

import lombok.*;
import vn.edu.fpt.dto.course.CourseDto;
import vn.edu.fpt.dto.user.UserDto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDto {
    private Integer id;
    private CourseDto course;
    private UserDto user;
    private BigDecimal progressPercent;
}

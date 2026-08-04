package vn.edu.fpt.dto.course;

import lombok.*;
import vn.edu.fpt.dto.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDto {
    private Integer id;
    private Integer rating;
    private String comment;
    private UserDto user;
    private LocalDateTime createdAt;
}

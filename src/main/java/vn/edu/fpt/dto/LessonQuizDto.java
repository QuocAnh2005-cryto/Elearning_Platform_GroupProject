package vn.edu.fpt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonQuizDto {
    private Integer id;
    private String title;
    private String status;
    private Integer questionCount;
}
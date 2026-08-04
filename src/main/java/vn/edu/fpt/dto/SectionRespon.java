package vn.edu.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionRespon {
    private Integer Id;
    private String title;
    private Integer position;
    private LocalDateTime createAt;
    private List<LessonRespon> lessons;
}

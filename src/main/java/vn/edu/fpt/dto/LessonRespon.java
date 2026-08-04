package vn.edu.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LessonRespon {
    private Integer Id;
    private String title;
    private Integer position;
    private LocalDateTime createAt;
    private String videoUrl;
    private Integer dutationSecond;
    private List<LessonMaterialRespon> materials;
}

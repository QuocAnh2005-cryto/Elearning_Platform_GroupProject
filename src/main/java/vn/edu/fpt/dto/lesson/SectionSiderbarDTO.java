package vn.edu.fpt.dto.lesson;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionSiderbarDTO {

    private Integer id;
    private String title;
    private Integer totalLessonBySection;
    private boolean completed;
    private List<LessonSiderbarDTO> lessons;
    private Integer position;

    public SectionSiderbarDTO(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public SectionSiderbarDTO(Integer id, String title, Integer position) {
        this.id = id;
        this.title = title;
        this.position = position;
    }
}


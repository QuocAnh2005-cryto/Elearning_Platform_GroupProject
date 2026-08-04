package vn.edu.fpt.dto.lesson;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonSiderbarDTO {

    private Integer id;
    private String title;
    private Integer durationSeconds;
    private String lessonUrl;
    private Integer position;
    private boolean completed = false;
    private boolean currentLesson = false;

    public LessonSiderbarDTO(Integer id, Integer durationSeconds, String title, Integer position, String lessonUrl) {
        this.lessonUrl = lessonUrl;
        this.id = id;
        this.durationSeconds = durationSeconds;
        this.title = title;
        this.position = position;
    }

    public String getDurationtext(){
        if(durationSeconds == null) return null;

        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        return hours > 0 ? hours + "h " + minutes + "m" : minutes + "m";
    }
}

package vn.edu.fpt.dto.lesson;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonNoteSiderbarDTO {

    private Integer id;
    private Integer videoTimeSeconds;
    private String lessonTitle;
    private Integer lessonId;
    private String noteContent;

    public String formatTime() {
        int h = (int) Math.floor(videoTimeSeconds / 3600);
        int m =  (int) Math.floor((videoTimeSeconds % 3600) / 60);
        int s =  (int) Math.floor(videoTimeSeconds % 60);

        String minus = String.valueOf(m);
        if (m < 10) {
            minus = "0" + minus;
        }
        String second = String.valueOf(s);
        if (s < 10) {
            second = "0" + second;
        }
        String hours = String.valueOf(h);
        if (h < 10) {
            hours = "0" + hours;
        }
        if (h > 0) {
            return hours + ":" + minus + ":" +  second;
        }
        return minus + ":" + second;
    }
}

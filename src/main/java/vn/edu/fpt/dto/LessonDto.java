package vn.edu.fpt.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.dto.quizdto.QuizDTO;
import vn.edu.fpt.entity.LessonMaterial;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {
    private Integer id;
    private String title;
    private String videoUrl;
    private Integer durationSeconds;
    private Integer position;
    List<LessonMaterialDto> materials;

    public String getDurationtext(){
        if(durationSeconds == null) return null;

        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        return hours > 0 ? hours + "h " + minutes + "m" : minutes + "m";
    }

    List<LessonQuizDto> quizzes;
}

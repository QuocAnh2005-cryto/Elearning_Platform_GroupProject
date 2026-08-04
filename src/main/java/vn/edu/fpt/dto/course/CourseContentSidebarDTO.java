package vn.edu.fpt.dto.course;

import lombok.*;
import vn.edu.fpt.dto.LessonDto;
import vn.edu.fpt.dto.LessonMaterialDto;
import vn.edu.fpt.dto.lesson.LessonNoteSiderbarDTO;
import vn.edu.fpt.dto.lesson.SectionSiderbarDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContentSidebarDTO {

    private Integer courseId;
    private String courseDescription;
    private Integer totalLesson;
    private Integer completedLesson;
    private Integer currentLessonId;
    private String currentLessonTitle;
    private String thumbanailURL;
    private Integer nextLessonId;
    private Integer nextSectionId;
    private String nextLessonTitle;
    private Double progressPercent;
    private LessonDto lesson;

    private List<LessonMaterialDto> materials;
    private Boolean lessonProgressStatus;
    private Boolean showReviewPrompt;
    private Integer currentSectionId;

    private List<SectionSiderbarDTO> sections;

    private List<LessonNoteSiderbarDTO> lessonNoteSiderbarDTOS;

}


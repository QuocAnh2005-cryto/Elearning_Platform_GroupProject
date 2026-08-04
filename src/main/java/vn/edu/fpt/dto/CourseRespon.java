package vn.edu.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRespon {
    private Integer Id;
    private String tittle;
    private String category;
    private String description;
    private String thumnaiUrl;
    private String introVideoUrl;
    private String introVideoPreviewUrl;
    private BigDecimal price;
    private String level;
    private LocalDateTime createAt;
    private List<SectionRespon> sections;

    public String getLevel() {
        if ("BEGINNER".equalsIgnoreCase(level)) {
            return "Cơ bản";
        } else if ("INTERMEDIATE".equalsIgnoreCase(level)) {
            return "Trung cấp";
        } else if ("ADVANCED".equalsIgnoreCase(level)) {
            return "Nâng cao";
        }
        return level;
    }
}

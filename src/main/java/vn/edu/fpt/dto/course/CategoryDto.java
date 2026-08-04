package vn.edu.fpt.dto.course;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Integer id;
    private String name;
    private String description;
    private String status;
    private Integer parentId;
    private Set<CategoryDto> children;
    private Integer courseCount;
}

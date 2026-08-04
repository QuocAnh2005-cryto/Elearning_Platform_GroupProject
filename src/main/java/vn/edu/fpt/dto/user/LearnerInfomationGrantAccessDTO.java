package vn.edu.fpt.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearnerInfomationGrantAccessDTO {
    private Integer id;
    private String avatarUrl;
    private String fullName;
    private String lastName;
    private String email;
    private String phone;
}

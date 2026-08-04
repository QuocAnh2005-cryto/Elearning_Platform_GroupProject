package vn.edu.fpt.dto.user;

import lombok.*;
import vn.edu.fpt.enums.RoleType;
import vn.edu.fpt.enums.UserStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class UserDto {
    private Integer id;
    private LocalDateTime createdAt;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private String bio;
    private String phone;
    private RoleType role;
    private UserStatus status;
    private Integer courseCount;
    private Boolean hasPassword;



    public String getFullAvatarUrl() {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return null;
        }

        if (avatarUrl.startsWith("http://")
                || avatarUrl.startsWith("https://")) {
            return avatarUrl;
        }

        return vn.edu.fpt.util.AppConstants.AZURE_STORAGE_BASE_URL + "/" +
                vn.edu.fpt.util.AppConstants.AZURE_STORAGE_CONTAINER_USER_AVATARS + "/" +
                avatarUrl;
    }

    public String getFullName() {
        return (lastName != null ? lastName : "") + " " + (firstName != null ? firstName : "");
    }

    public String getStatusLabel() {
        return status != null ? status.getLabel() : "";
    }
}

package vn.edu.fpt.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileDto {
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 kí tự")
    private String firstname;

    @NotBlank(message = "Họ không được để trống")
    @Size(min = 2, max = 50, message = "Họ phải từ 2 đến 50 kí tự")
    private String lastname;

    @Size(min = 20, max = 500, message = "Mô tả phải từ 20 đến 500 kí tự")
    private String bio;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\\\+84)[0-9]{9,10}$", message = "Vui lòng nhập đúng định dạng số điện thoại")
    private String phone;

    private MultipartFile file;
    private String avatar_url;
    private String email;

}

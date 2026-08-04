package vn.edu.fpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDto {

    @NotBlank
    private String token;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\W).*$",
            message = "Mật khẩu phải có ít nhất 1 chữ hoa và 1 ký tự đặc biệt"
    )
    private String password;

    @NotBlank(message = "Retype password không được để trống")
    private String confirmPassword;

    public boolean isPasswordMatched() {
        return password != null
                && password.equals(confirmPassword);
    }

}

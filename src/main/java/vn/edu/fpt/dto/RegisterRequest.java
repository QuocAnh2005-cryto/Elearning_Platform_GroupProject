package vn.edu.fpt.dto;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        private String email;

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\W).*$", message = "Mật khẩu phải có ít nhất 1 chữ hoa và 1 ký tự đặc biệt")
        private String password;

        @NotBlank(message = "Họ không được để trống")
        private String firstName;

        @NotBlank(message = "Tên không được để trống")
        private String lastName;

        @NotBlank(message = "Retype password không được để trống")
        private String confirmPassword;

        @Pattern(regexp = "^$|^(03|05|07|08|09)\\d{8}$", message = "Số điện thoại không hợp lệ")
        private String phoneNumber;

        public boolean isPasswordMatched() {
                return password != null
                                && password.equals(confirmPassword);
        }
}

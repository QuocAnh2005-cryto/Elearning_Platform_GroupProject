package vn.edu.fpt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ForgotPasswordRequestDto {



    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;


}

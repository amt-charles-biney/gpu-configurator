package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.annotation.PasswordsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordsMatch(message = "password and confirm password are not equal")
public class ChangePasswordDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "OTP code cannot be blank")
    public String otpCode;

    @NotBlank(message = "New password cannot be blank")
    public String newPassword;

    @NotBlank(message = "Confirm new password cannot be blank")
    public String confirmNewPassword;
}
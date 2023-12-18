package com.amalitech.gpuconfigurator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "OTP code cannot be blank")
    public String otpCode;
}

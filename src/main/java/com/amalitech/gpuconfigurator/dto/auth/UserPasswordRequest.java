package com.amalitech.gpuconfigurator.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPasswordRequest {
    @NotBlank
    public String currentPassword;

    @NotBlank
    public String newPassword;

    @NotBlank
    public String confirmNewPassword;
}
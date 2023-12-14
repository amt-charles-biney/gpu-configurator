package com.amalitech.gpuconfigurator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = "First Name is required")
    @NotNull(message = "First Name cannot be null")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    @NotNull(message = "Last Name cannot be null")
    private String lastName;
    @Email(message = "Format should be an email")
    @NotBlank(message = "Email cannot be null")
    @NotNull(message = "Email cannot be null ")
    private String email;
    @Size(min = 4, message = "Password must be a minimum of 4 characters")
    private String password;
}

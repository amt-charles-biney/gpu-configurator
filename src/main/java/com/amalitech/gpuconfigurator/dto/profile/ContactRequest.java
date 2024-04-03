package com.amalitech.gpuconfigurator.dto.profile;

import com.amalitech.gpuconfigurator.annotation.IsValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IsValidPhoneNumber
public class ContactRequest {
    @NotBlank
    public String phoneNumber;

    @NotBlank
    public String country;

    @NotBlank
    public String iso2Code;

    @NotBlank
    public String dialCode;
}

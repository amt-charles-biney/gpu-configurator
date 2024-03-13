package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ContactRequest(
        @NotBlank(message = "country cannot be blank")
        String country,

        @NotBlank(message = "dialCode cannot be blank")
        String dialCode,
        @NotBlank(message = "isoCode cannot be blank")
        String iso2Code,

        @NotBlank(message = "phone number should not be blank")
        @Pattern(regexp = "^(\\+\\d{1,3})?\\d{10}$", message = "Invalid phone number")
        String phoneNumber) {
}

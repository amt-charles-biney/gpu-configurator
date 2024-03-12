package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MobileMoneyRequest(
        @NotBlank(message = "phone number should not be blank")
        @Pattern(regexp = "^(\\+\\d{1,3})?\\d{10}$", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank(message = "network cannot be blank")
        String network

) {
}

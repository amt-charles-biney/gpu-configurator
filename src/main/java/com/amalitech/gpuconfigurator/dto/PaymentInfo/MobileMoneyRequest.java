package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import jakarta.validation.constraints.NotBlank;

public record MobileMoneyRequest(
        @NotBlank(message = "phone number should not be blank")
        String phoneNumber) {
}

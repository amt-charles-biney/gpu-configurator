package com.amalitech.gpuconfigurator.dto.Payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyPaymentRequest(
        @NotNull(message = "reference cannot be null")
                @NotBlank(message = "reference cannot be blank")
        String reference
) {
}

package com.amalitech.gpuconfigurator.dto.Payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InitializePaymentRequest(
        @NotNull(message = "email cannot be null")
                @NotBlank(message = "email cannot be blank")
        String email,

        @NotNull(message = "amount cannot be null")
        double amount,

        @NotNull(message = "currency cannot be null")
                @NotBlank(message = "currency cannot be blank")
        String currency,

        String reference
) {
}

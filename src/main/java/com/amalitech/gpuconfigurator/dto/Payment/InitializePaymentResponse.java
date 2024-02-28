package com.amalitech.gpuconfigurator.dto.Payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InitializePaymentResponse(
        boolean status,
        @NotBlank(message = "Message must not be blank")
        String message,
        Data data
) {
    public record Data(
            @NotBlank(message = "Authorization URL must not be blank")
            String authorizationUrl,
            @NotBlank(message = "Access code must not be blank")
            String accessCode,
            @NotBlank(message = "Reference must not be blank")
            String reference
    ) {}
}

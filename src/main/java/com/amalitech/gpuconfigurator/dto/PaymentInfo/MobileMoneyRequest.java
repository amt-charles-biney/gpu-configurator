package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MobileMoneyRequest(

        ContactRequest contact,
        @NotBlank(message = "network cannot be blank")
        String network

) {
}

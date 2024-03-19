package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CardInfoRequest(
        @Pattern(regexp = "^\\d{16}$", message = "Invalid card number")
        @NotBlank(message = "card Number cannot be blank")
        String cardNumber,
        @NotBlank(message = "expiration date cannot be blank")
        String expirationDate,
        @NotBlank(message= "payment method cannot be blank")
        String paymentMethod,
        @NotBlank(message = "card holder name cannot be blank")
        String cardHolderName) {

}

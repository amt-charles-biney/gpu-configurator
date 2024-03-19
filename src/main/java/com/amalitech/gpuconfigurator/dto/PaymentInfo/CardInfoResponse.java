package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import lombok.Builder;

@Builder
public record CardInfoResponse(String id, String cardNumber, String expirationDate, String cardHolderName, String paymentMethod) {
}

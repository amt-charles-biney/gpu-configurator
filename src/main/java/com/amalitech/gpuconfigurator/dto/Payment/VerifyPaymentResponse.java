package com.amalitech.gpuconfigurator.dto.Payment;

import lombok.Builder;

@Builder
public record VerifyPaymentResponse(String orderId, String message, int status) {
}

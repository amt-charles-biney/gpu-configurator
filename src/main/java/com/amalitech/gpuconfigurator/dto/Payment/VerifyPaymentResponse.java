package com.amalitech.gpuconfigurator.dto.Payment;

import lombok.Builder;

@Builder
public record VerifyPaymentResponse(String trackingId, String trackingUrl, String message, int status) {
}

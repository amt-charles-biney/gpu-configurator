package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import lombok.Builder;

@Builder
public record MobileMoneyResponse(String phoneNumber, String id, String network) {
}

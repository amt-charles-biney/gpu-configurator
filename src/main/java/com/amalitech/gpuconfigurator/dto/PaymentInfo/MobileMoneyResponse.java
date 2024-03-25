package com.amalitech.gpuconfigurator.dto.PaymentInfo;

import lombok.Builder;

@Builder
public record MobileMoneyResponse(ContactRequest contact, String id, String network) {
}

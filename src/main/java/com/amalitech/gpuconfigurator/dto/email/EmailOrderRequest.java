package com.amalitech.gpuconfigurator.dto.email;

import lombok.Builder;

@Builder
public record EmailOrderRequest(String email, String orderStatus, String orderId, String message) {
}

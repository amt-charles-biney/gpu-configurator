package com.amalitech.gpuconfigurator.dto.order;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderDto(UUID orderId, String trackingUrl, String trackingId) {
}

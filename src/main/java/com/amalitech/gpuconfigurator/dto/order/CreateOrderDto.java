package com.amalitech.gpuconfigurator.dto.order;

import com.shippo.model.Shipment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderDto(UUID orderId, String trackingUrl, String trackingId) {
}

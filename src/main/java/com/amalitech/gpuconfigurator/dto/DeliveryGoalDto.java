package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

@Builder
public record DeliveryGoalDto(
        Float percentage,
        Long totalDeliveredItems
) {
}

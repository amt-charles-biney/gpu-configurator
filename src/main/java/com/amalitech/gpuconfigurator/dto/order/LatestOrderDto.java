package com.amalitech.gpuconfigurator.dto.order;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LatestOrderDto(
        String productName,
        String coverImage,
        LocalDate orderedTime,
        String status
) {
}

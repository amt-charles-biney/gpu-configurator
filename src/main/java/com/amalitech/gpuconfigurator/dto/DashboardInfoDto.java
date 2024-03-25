package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

@Builder
public record DashboardInfoDto(
        Integer customers
) {
}

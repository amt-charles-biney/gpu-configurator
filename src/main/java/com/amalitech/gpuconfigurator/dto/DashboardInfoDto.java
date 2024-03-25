package com.amalitech.gpuconfigurator.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DashboardInfoDto(
        Long customers,
        Long orders,
        Long products,
        BigDecimal revenue
) {
}

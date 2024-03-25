package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.dto.order.LatestOrderDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record DashboardInfoDto(
        Long customers,
        Long orders,
        Long products,
        BigDecimal revenue,
        List<LatestOrderDto> latestOrders
) {
}

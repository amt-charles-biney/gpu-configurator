package com.amalitech.gpuconfigurator.dto.customers;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CustomerResponseDto(
        String name,
        Integer numberOfOrders,

        BigDecimal amountSpent,

        LocalDate joinDate
) {
}

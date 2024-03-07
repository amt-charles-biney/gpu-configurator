package com.amalitech.gpuconfigurator.dto.order;

import com.amalitech.gpuconfigurator.model.OrderType;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record OrderResponseDto(
        String productCoverImage,
        Set<Configuration> products,
        UUID orderId,
        String customerName,
        String paymentMethod,
        OrderType status,
        LocalDateTime date,
        BigDecimal totalPrice
        ) {
}

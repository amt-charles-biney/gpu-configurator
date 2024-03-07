package com.amalitech.gpuconfigurator.dto.order;

import com.amalitech.gpuconfigurator.model.OrderType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderPageResponseDto(
        String productCoverImage,
        String productName,
        UUID orderId,
        String customerName,
        String paymentMethod,
        OrderType status,
        LocalDateTime date,
        BigDecimal totalPrice
        ) {
}

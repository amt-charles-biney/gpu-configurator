package com.amalitech.gpuconfigurator.dto.order;


import com.amalitech.gpuconfigurator.model.OrderType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record OrderResponseDto(
        UUID orderId,
        String productImageCover,

        String productName,

        String customerName,
        String paymentMethod,

        OrderType status,

        BigDecimal totalPrice,

        LocalDateTime date

) {

}

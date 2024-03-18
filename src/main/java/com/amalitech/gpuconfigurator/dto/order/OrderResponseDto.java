package com.amalitech.gpuconfigurator.dto.order;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.OrderType;
import com.amalitech.gpuconfigurator.model.Shipping;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Builder
public record OrderResponseDto(

        UUID id,
        String productCoverImage,
        String productName,
        Set<Configuration> configuredProduct,
        String orderId,
        String trackingUrl,
        String customerName,
        String paymentMethod,
        String status,
        LocalDate date,
        BigDecimal totalPrice,

        String estArrival,

        String brandName,

        String shippingAddress
) {
}

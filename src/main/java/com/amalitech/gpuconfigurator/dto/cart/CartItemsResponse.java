package com.amalitech.gpuconfigurator.dto.cart;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;

public record CartItemsResponse(
        Iterable<ConfigurationResponseDto> configuredProducts,
        long count
) {
}

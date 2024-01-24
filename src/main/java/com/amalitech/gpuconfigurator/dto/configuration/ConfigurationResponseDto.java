package com.amalitech.gpuconfigurator.dto.configuration;


import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder

public record ConfigurationResponseDto(String Id, BigDecimal totalPrice, String productId, String productName,
                                       BigDecimal productPrice,
                                       BigDecimal configuredPrice, List<ConfigOptions> configured, BigDecimal vat,
                                       Boolean warranty) {
}

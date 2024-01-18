package com.amalitech.gpuconfigurator.dto.configuration;


import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
<<<<<<< HEAD
<<<<<<< HEAD
public record ConfigurationResponseDto(String id, BigDecimal totalPrice, String productId,BigDecimal productPrice, BigDecimal configuredPrice , List<ConfigOptions> configured) {
=======
public record ConfigurationResponseDto( BigDecimal totalPrice, String productId,BigDecimal productPrice, BigDecimal configuredPrice , List<ConfigOptions> configured, BigDecimal vat) {
>>>>>>> 047ae9b (feat: configuring items)
=======
public record ConfigurationResponseDto( BigDecimal totalPrice, String productId,BigDecimal productPrice, BigDecimal configuredPrice , List<ConfigOptions> configured, BigDecimal vat, Boolean warranty) {
>>>>>>> ae0e1a8 (feat: configuration)
}

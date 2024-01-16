package com.amalitech.gpuconfigurator.dto.configuration;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record ConfigurationResponseDto(BigDecimal totalPrice, ProductDetailsDto product,Map<String, List<configOptionDto>> configured ) {}

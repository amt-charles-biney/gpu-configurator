package com.amalitech.gpuconfigurator.dto.configuration;

import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ConfigurationResponseDto(@JsonProperty("id") String Id, BigDecimal totalPrice, String productId,
                                       String productName, BigDecimal productPrice, String productDescription,
                                       String productCoverImage, String productCaseName,
                                       BigDecimal configuredPrice, List<ConfigOptions> configured, BigDecimal vat,
                                       String coverImage, String configurationUrl,

                                       Boolean warranty, int stock, int quantity, boolean productAvailability) {
}

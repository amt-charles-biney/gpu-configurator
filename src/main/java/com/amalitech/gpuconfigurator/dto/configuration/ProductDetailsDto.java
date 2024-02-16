package com.amalitech.gpuconfigurator.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDto {
    private String id;
    private String productName;
    private BigDecimal productPrice;
}

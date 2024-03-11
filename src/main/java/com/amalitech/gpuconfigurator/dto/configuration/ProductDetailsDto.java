package com.amalitech.gpuconfigurator.dto.configuration;

import lombok.*;

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

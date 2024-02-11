package com.amalitech.gpuconfigurator.dto.cases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeOptionResponse {
    private UUID id;

    private String optionName;

    private BigDecimal priceAdjustment;

    private String media;

    private Float baseAmount;

    private Double priceFactor;
}

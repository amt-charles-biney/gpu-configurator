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
public class configOptionDto {
    private String optionId;

    private String optionName;

    private String optionType;

    private BigDecimal optionPrice;

    private Boolean isIncluded;
}

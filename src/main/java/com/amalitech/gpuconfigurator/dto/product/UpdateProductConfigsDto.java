package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UpdateProductConfigsDto {
    Integer inStock;
    BigDecimal baseConfigPrice;
}

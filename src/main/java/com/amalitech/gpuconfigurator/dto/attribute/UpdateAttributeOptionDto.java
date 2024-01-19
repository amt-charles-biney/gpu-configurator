package com.amalitech.gpuconfigurator.dto.attribute;

import java.math.BigDecimal;

public record UpdateAttributeOptionDto(String id, String name, BigDecimal price, String media, Float baseAmount, Float maxAmount, Double priceFactor) {
}

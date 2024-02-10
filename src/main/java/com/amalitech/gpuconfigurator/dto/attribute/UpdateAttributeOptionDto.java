package com.amalitech.gpuconfigurator.dto.attribute;

import java.math.BigDecimal;
import java.util.List;

public record UpdateAttributeOptionDto(String id, String name, BigDecimal price, String brand, List<String> incompatibleAttributeOptions, String media, Float baseAmount, Float maxAmount, Double priceFactor, Integer inStock) {
}

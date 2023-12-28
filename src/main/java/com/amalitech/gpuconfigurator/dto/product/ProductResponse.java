package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.model.Category;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(String id, String productName, BigDecimal productPrice, String imageUrl, String productId, Category category, String productDescription) {
}

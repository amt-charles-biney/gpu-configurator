package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record FeaturedProductDto(

        String id,
        String productName,
        String coverImage,

        BigDecimal productPrice,

        String productBrand,
        boolean productAvailability,
        boolean isWishListItem
        ) {
}

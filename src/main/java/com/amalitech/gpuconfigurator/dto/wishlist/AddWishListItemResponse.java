package com.amalitech.gpuconfigurator.dto.wishlist;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;

public record AddWishListItemResponse(ConfigurationResponseDto configuredProduct, String message) {
}

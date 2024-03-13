package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;

import java.util.List;
import java.util.UUID;

public interface FeaturedService {
    List<FeaturedProductDto> getAllFeaturedProduct();

    FeaturedResponseDto addFeaturedProduct(UUID id);

    FeaturedResponseDto removeFeaturedProduct(UUID id);
}

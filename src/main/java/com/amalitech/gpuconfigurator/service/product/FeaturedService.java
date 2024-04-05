package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;

import java.util.List;
import java.util.UUID;

public interface FeaturedService {
    List<FeaturedProductDto> getAllFeaturedProduct(User user, UserSession userSession);

    FeaturedResponseDto addFeaturedProduct(UUID id);

    FeaturedResponseDto removeFeaturedProduct(UUID id);
}

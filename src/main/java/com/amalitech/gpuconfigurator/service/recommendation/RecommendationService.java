package com.amalitech.gpuconfigurator.service.recommendation;

import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.repository.FeaturedProductAbstraction;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<FeaturedProductDto> getRecommendation(Principal principal, UserSession userSession);
}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.repository.FeaturedProductAbstraction;
import com.amalitech.gpuconfigurator.service.recommendation.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
            summary = "Get recommendation",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/recommendation")
    public ResponseEntity<List<FeaturedProductDto>> getCartItems(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendation(principal, userSession));
    }

}

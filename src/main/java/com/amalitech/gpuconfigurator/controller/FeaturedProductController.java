package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.product.FeaturedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeaturedProductController {
    private final FeaturedService featuredService;

    @Operation(
            summary = "Get all featured products",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/featured")
    @ResponseStatus(HttpStatus.OK)
    public List<FeaturedProductDto> getAllFeaturedProduct() {
        return featuredService.getAllFeaturedProduct();
    }

    @Operation(
            summary = "Add featured product",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/featured/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public FeaturedResponseDto addFeaturedProduct(@PathVariable("id") UUID id) {
        return featuredService.addFeaturedProduct(id);
    }

    @Operation(
            summary = "Remove featured product",
            method = "PUT"
    )
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/v1/admin/featured/{id}")
    public FeaturedResponseDto removeFeaturedProduct(@PathVariable("id") UUID id) {
        return featuredService.removeFeaturedProduct(id);
    }
}

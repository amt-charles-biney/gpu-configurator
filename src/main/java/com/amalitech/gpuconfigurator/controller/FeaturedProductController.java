package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.product.FeaturedService;
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

    @GetMapping("/v1/featured")
    @ResponseStatus(HttpStatus.OK)
    public List<FeaturedProductDto> getAllFeaturedProduct(){
        return featuredService.getAllFeaturedProduct();
    }

    @PostMapping("/v1/admin/featured/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public FeaturedResponseDto addFeaturedProduct(@PathVariable("id") UUID id){
        return featuredService.addFeaturedProduct(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/v1/admin/featured/{id}")
    public FeaturedResponseDto removeFeaturedProduct(@PathVariable("id") UUID id){
        return featuredService.removeFeaturedProduct(id);
    }
}

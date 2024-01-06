package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.featured.FeaturedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeaturedProductController {
    private final FeaturedService featuredService;

    @CrossOrigin
    @GetMapping("/v1/featured")
    public List<Product> getAllFeaturedProduct(){
        return featuredService.getAllFeaturedProduct();
    }

    @CrossOrigin
    @PostMapping("/v1/admin/featured/{id}")
    public FeaturedResponseDto addFeaturedProduct(@PathVariable("id") UUID id){
        return featuredService.addFeaturedProduct(id);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/featured/{id}")
    public FeaturedResponseDto removeFeaturedProduct(@PathVariable("id") UUID id){
        return featuredService.removeFeaturedProduct(id);
    }
}

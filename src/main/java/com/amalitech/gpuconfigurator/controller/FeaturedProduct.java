package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.product.FeaturedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeaturedProduct {
    private final FeaturedService featuredService;

    @CrossOrigin
    @GetMapping("/v1/featured")
    public List<Product> getAllFeaturedProduct(){
        return featuredService.getAllFeaturedProduct();
    }
}

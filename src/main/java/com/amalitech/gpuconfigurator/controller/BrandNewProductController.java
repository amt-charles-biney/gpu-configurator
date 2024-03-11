package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandNewProductController {

    private final ProductService productService;

    @GetMapping("/v1/new")
    @ResponseStatus(HttpStatus.OK)
    public List<FeaturedProductDto> getBrandNewProducts() {
        return productService.getNewProducts();
    }
}

package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.model.product.Product;
import com.amalitech.gpuconfigurator.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping("/v1/product")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProductResponseDto addProduct(@Valid @RequestBody ProductDto request){
        return productService.createProduct(request);
    }

    @GetMapping("/v1/product")
    public List<Product> getAllProducts(){
        return productService.getAllProduct();
    }

    @GetMapping("/v1/product/{name}")
    public List<Product> getProductByName(@PathVariable("name") String name){
        return productService.getProductByName(name);
    }

    @DeleteMapping("/v1/product/{id}")
    public void deleteProduct(@PathVariable("id")UUID id) {
        productService.deleteProductById(id);
    }
}

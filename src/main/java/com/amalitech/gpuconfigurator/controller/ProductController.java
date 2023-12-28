package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.ProductServiceImpl;
import com.amalitech.gpuconfigurator.service.UploadImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UploadImageService cloudinaryImage;


    @CrossOrigin
    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)

    public CreateProductResponseDto addProduct(@Valid @ModelAttribute ProductDto request,
                                               @RequestParam("file") List<MultipartFile> files) {
        return productService.createProduct(request, files);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products =  productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable("productId") String productId){
        ProductResponse product =  productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id")UUID id) {
        productService.deleteProductById(id);
    }


}

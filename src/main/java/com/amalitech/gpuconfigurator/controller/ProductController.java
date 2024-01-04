package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageService;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
                                               @RequestParam("file") List<MultipartFile> files, @RequestParam("coverImage") MultipartFile coverImage) {
        return productService.createProduct(request, files, coverImage);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable("productId") String productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            Page<ProductResponse> products = productService.getAllProducts(page, size);
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(products.getContent());
        } else {
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        }
    }


    @PatchMapping("/v1/admin/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @Valid @ModelAttribute ProductDto updatedProductDto) {

        ProductResponse updatedProduct = productService.updateProduct(id, updatedProductDto);

        return ResponseEntity.ok(updatedProduct);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }


}
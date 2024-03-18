package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;


    @CrossOrigin
    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)

    public CreateProductResponseDto addProduct(@Valid @RequestBody ProductDto request) {
        return productService.createProduct(request);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponseWithBrandDto> getProductByProductId(@PathVariable("productId") String productId) {
        ProductResponseWithBrandDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @CrossOrigin
    @GetMapping("/v1/product/{productId}")
    public ResponseEntity<ProductResponseWithBrandDto> getProductByProductIdUser(@PathVariable("productId") String productId) {
        ProductResponseWithBrandDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size
    ) {
        Page<ProductResponse> resultPage = productService.getAllProductsAdmin(page, size);

        return ResponseEntity.ok(resultPage);
    }

    @CrossOrigin
    @GetMapping("/v1/product")
    public ResponseEntity<Page<ProductResponse>> getAllProductUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") List<String> productCase,
            @RequestParam(defaultValue = "") List<String> price,
            @RequestParam(defaultValue = "") List<String> brand,
            @RequestParam(defaultValue = "") List<String> categories,
            @RequestParam(defaultValue = "") String query) {
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .query(query)
                .cases(productCase)
                .prices(price)
                .brands(brand)
                .categories(categories)
                .build();

        Page<ProductResponse> response = productService.getAllProductsUsers(page, size, productSearchRequest);

        return ResponseEntity.ok(response);
    }


    @CrossOrigin
    @PatchMapping("/v1/admin/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @RequestBody ProductUpdateDto updatedProductDto
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, updatedProductDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/product/all")
    public ResponseEntity<GenericResponse> deleteAllProducts(@RequestBody List<String> productIds) {
        GenericResponse deletedBulkProduct = productService.deleteBulkProducts(productIds);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkProduct);
    }


}

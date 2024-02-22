package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageService;
import com.amalitech.gpuconfigurator.service.product.FilteringService;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import com.amalitech.gpuconfigurator.service.search.SearchService;
import com.amalitech.gpuconfigurator.util.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UploadImageService cloudinaryImage;
    private final FilteringService filteringService;
    private final SearchService searchService;


    @CrossOrigin
    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)

    public CreateProductResponseDto addProduct(@Valid @RequestBody ProductDto request) {
        return productService.createProduct(request);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable("productId") String productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @CrossOrigin
    @GetMapping("/v1/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductIdUser(@PathVariable("productId") String productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<PageResponseDto> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {

        PageResponseDto productsResponse = new PageResponseDto();

        if (page != null && size != null) {
            Page<ProductResponse> products = productService.getAllProductsAdmin(page, size, sort);
            productsResponse.setProducts(products.getContent());
            productsResponse.setTotal(products.getTotalElements());
        } else {
            List<ProductResponse> products = productService.getAllProducts();
            productsResponse.setProducts(products);
            productsResponse.setTotal(products.size());
        }

        return ResponseEntity.ok(productsResponse);
    }




    @CrossOrigin
    @GetMapping("/v1/product")
    public ResponseEntity<PageResponseDto> getAllProductUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String productCase,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String processor,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category) {
        if (query != null && !query.isBlank()) {
            String[] brands = null;
            String[] priceRanges = null;
            if (productCase != null && !productCase.isBlank()) {
                brands = productCase.strip().split(",");
            }
            if (price != null && !price.isBlank()) {
                priceRanges = price.strip().split(",");
            }
            return ResponseEntity.ok(
                    searchService.findProducts(
                            query,
                            page,
                            size == null ? 10 : size,
                            sort == null ? "createdAt" : sort,
                            brands,
                            priceRanges
                    )
            );
        }

        PageResponseDto productsResponse = new PageResponseDto();

        List<ProductResponse> products = new ArrayList<>();

        if (productCase != null || price != null || productType != null || processor != null || category != null) {
            List<Product> filteredProducts = filteringService.filterProduct(productCase, price, productType, processor,category);
            if (!filteredProducts.isEmpty()) {
                products = new ResponseMapper().getProductResponses(filteredProducts);
                productsResponse.setProducts(products);
                productsResponse.setTotal(products.size());
            } else {
                productsResponse.setProducts(Collections.emptyList());
                productsResponse.setTotal(0);
            }

        } else if (page != null && size != null) {

            Page<ProductResponse> pagedProducts = productService.getAllProducts(page, size, sort);
            products = pagedProducts.getContent();
            productsResponse.setTotal(pagedProducts.getTotalElements());

        } else {
            products = productService.getAllProducts();
            productsResponse.setProducts(products);
            productsResponse.setTotal(products.size());
        }

        productsResponse.setProducts(products);
        return ResponseEntity.ok(productsResponse);
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

package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @Operation(
            summary = "Add product",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProductResponseDto addProduct(@Valid @RequestBody ProductDto request) {
        return productService.createProduct(request);
    }

    @Operation(
            summary = "Get product by product ID (for admin)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponseWithBrandDto> getProductByProductId(@PathVariable("productId") String productId) {
        ProductResponseWithBrandDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Get product by product ID (for user)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/product/{productId}")
    public ResponseEntity<ProductResponseWithBrandDto> getProductByProductIdUser(@PathVariable("productId") String productId) {
        ProductResponseWithBrandDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Get all products (for admin)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam(defaultValue = "") String q
    ) {
        Page<ProductResponse> resultPage = productService.getAllProductsAdmin(page, size, q);
        return ResponseEntity.ok(resultPage);
    }

    @Operation(
            summary = "Get all products (for user)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/product")
    public ResponseEntity<Page<ProductResponse>> getAllProductUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") List<String> productCase,
            @RequestParam(defaultValue = "") List<String> price,
            @RequestParam(defaultValue = "") List<String> brand,
            @RequestParam(defaultValue = "") List<String> categories,
            @RequestParam(defaultValue = "") String query,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .query(query)
                .cases(productCase.stream().map(String::strip).toList())
                .prices(price)
                .brands(brand.stream().map(String::strip).toList())
                .categories(categories.stream().map(String::strip).toList())
                .build();

        User user = principal == null ? null : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        Page<ProductResponse> response = productService.getAllProductsUsers(page, size, productSearchRequest, user, userSession);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update product",
            method = "PATCH"
    )
    @CrossOrigin
    @PatchMapping("/v1/admin/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @RequestBody ProductUpdateDto updatedProductDto
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, updatedProductDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(
            summary = "Delete product by ID",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }

    @Operation(
            summary = "Delete all products",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/product/all")
    public ResponseEntity<GenericResponse> deleteAllProducts(@RequestBody List<String> productIds) {
        GenericResponse deletedBulkProduct = productService.deleteBulkProducts(productIds);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkProduct);
    }

}

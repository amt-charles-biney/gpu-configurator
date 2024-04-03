package com.amalitech.gpuconfigurator.controller.compare;


import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductResponseDto;
import com.amalitech.gpuconfigurator.service.compare.CompareServiceImpl;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Compare")
public class CompareController {

    private final CompareServiceImpl compareService;
    private final ProductServiceImpl productService;

    @Operation(
            description = "Get compare product",
            summary = "This is the endpoint for getting all compared product",
            method = "GET"
    )
    @GetMapping("/v1/compare")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllCompareProduct() {
        List<ProductResponseDto> productResponseDto = productService.getAllProductsCompare();
        return ResponseEntity.ok(new ApiResponse<>(productResponseDto));
    }

    @Operation(
            description = "Get compare product",
            summary = "This is the endpoint for getting all compared product as list",
            method = "GET"
    )
    @GetMapping("/v1/compare/all")
    public ResponseEntity<ApiResponse<List<ProductCompareResponse>>> getAllCompareProductsList(@RequestParam(required = false) List<String> products) {
        if (products == null) {
            return ResponseEntity.ok(new ApiResponse<>(new ArrayList<>(), "no products to compare", "done"));
        }
        List<ProductCompareResponse> productCompareResponses = compareService.getProductCompareList(products);
        return ResponseEntity.ok(new ApiResponse<>(productCompareResponses));
    }


    @Operation(
            description = "Compare product",
            summary = "Compare products by their Ids",
            method = "GET"
    )

    @GetMapping("/v1/compare/{productId}")
    public ResponseEntity<ApiResponse<ProductCompareResponse>> compareProductById(@PathVariable("productId") String productId) {
        ProductCompareResponse productCompareResponse = compareService.getProductCompare(productId);
        return ResponseEntity.ok(new ApiResponse<>(productCompareResponse));
    }

}

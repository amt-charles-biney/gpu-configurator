package com.amalitech.gpuconfigurator.controller.compare;


import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductResponseDto;
import com.amalitech.gpuconfigurator.service.compare.CompareServiceImpl;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompareController {

    private final CompareServiceImpl compareService;
    private final ProductServiceImpl productService;

    @GetMapping("/v1/compare")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllCompareProduct() {
        List<ProductResponseDto> productResponseDto = productService.getAllProductsCompare();
        return ResponseEntity.ok(new ApiResponse<>(productResponseDto));
    }

    @GetMapping("/v1/compare/{productId}")
    public ResponseEntity<ApiResponse<ProductCompareResponse>> compareProductById(@PathVariable("productId") String productId) {
        ProductCompareResponse productCompareResponse = compareService.getProductCompare(productId);
        return ResponseEntity.ok(new ApiResponse<>(productCompareResponse));
    }

}

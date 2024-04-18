package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryConfigController {

    private final CategoryConfigServiceImpl categoryConfigServiceImpl;

    @Operation(
            summary = "Add category configuration",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/category/config")
    public ResponseEntity<GenericResponse> addConfig(@RequestBody CategoryConfigRequest categoryConfigDto) {
        GenericResponse result = categoryConfigServiceImpl.createCategoryConfig(categoryConfigDto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get category configurations by category ID",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigs(@PathVariable String categoryId) {
        CategoryConfigResponseDto result = categoryConfigServiceImpl.getCategoryConfigByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get category configurations by product ID",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/category/{productId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigByProductId(@PathVariable String productId) {
        CategoryConfigResponseDto result = categoryConfigServiceImpl.getCategoryConfigByProduct(productId);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get all category configurations pageable",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/category/config")
    public ResponseEntity<Page<CategoryListResponse>> getAllCategoryConfigPageable(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String q
    ) {
        Page<CategoryListResponse> result = categoryConfigServiceImpl.getCategoryListResponsesPageable(size, page, q);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get all category configurations by category ID",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<CompatibleOptionGetResponse> getAllCategoryConfigById(@PathVariable String categoryId) {
        CompatibleOptionGetResponse result = categoryConfigServiceImpl.getCategoryAndCompatibleOption(UUID.fromString(categoryId));
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Update category configuration",
            method = "PUT"
    )
    @CrossOrigin
    @PutMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<CompatibleOptionGetResponse> update(@RequestBody CompatibleOptionEditResponse compatibleOptionEditResponse) {
        CompatibleOptionGetResponse result = categoryConfigServiceImpl.updateCategoryAndConfigs(compatibleOptionEditResponse);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Delete all category configurations",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/category/config")
    public ResponseEntity<GenericResponse> deleteAllCategory(@RequestBody List<String> categoryIds) {
        GenericResponse result = categoryConfigServiceImpl.deleteCategoryAndCategoryConfig(categoryIds);
        return ResponseEntity.ok(result);
    }
}

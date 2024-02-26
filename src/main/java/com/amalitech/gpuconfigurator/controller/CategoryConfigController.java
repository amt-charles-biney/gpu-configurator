package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryConfigController {

    private final CategoryConfigServiceImpl categoryConfigServiceImpl;

    @CrossOrigin
    @PostMapping("/v1/admin/category/config")
    public ResponseEntity<GenericResponse> addConfig(@RequestBody CategoryConfigRequest categoryConfigDto) {
        GenericResponse result = categoryConfigServiceImpl.createCategoryConfig(categoryConfigDto);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigs(@PathVariable String categoryId) {
        CategoryConfigResponseDto result = categoryConfigServiceImpl.getCategoryConfigByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/category/{productId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigByProductId(@PathVariable String productId) {
        CategoryConfigResponseDto result = categoryConfigServiceImpl.getCategoryConfigByProduct(productId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/config")
    public ResponseEntity<List<CategoryListResponse>> getAllCategoryConfig() {
        List<CategoryListResponse> result = categoryConfigServiceImpl.getCategoryListResponses();
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<CompatibleOptionGetResponse> getAllCategoryConfigById(@PathVariable String categoryId) {
        CompatibleOptionGetResponse result = categoryConfigServiceImpl.getCategoryAndCompatibleOption(UUID.fromString(categoryId));
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<GenericResponse> update(@RequestBody CompatibleOptionEditResponse compatibleOptionEditResponse) {
        GenericResponse result = categoryConfigServiceImpl.updateCategoryAndConfigs(compatibleOptionEditResponse);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/category/config")
    public ResponseEntity<GenericResponse> deleteAllCategory(@RequestBody List<String> categoryIds) {
        GenericResponse result = categoryConfigServiceImpl.deleteCategoryAndCategoryConfig(categoryIds);
        return ResponseEntity.ok(result);
    }
}

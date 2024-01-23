package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryListResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionEditResponse;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryConfigController {

    private final CategoryConfigService categoryConfigService;

    @CrossOrigin
    @PostMapping("/v1/admin/category/config")
    public ResponseEntity<GenericResponse> addConfig(@RequestBody CategoryConfigRequest categoryConfigDto) {
        GenericResponse result = categoryConfigService.createCategoryConfig(categoryConfigDto);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigs(@PathVariable String categoryId) {
        CategoryConfigResponseDto result = categoryConfigService.getCategoryConfigByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/category/{categoryId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigsUser(@PathVariable String categoryId) {
        CategoryConfigResponseDto result = categoryConfigService.getCategoryConfigByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/config")
    public ResponseEntity<List<CategoryListResponse>> getAllCategoryConfig() {
        List<CategoryListResponse> result = categoryConfigService.getCategoryListResponses();
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<CompatibleOptionEditResponse> getAllCategoryConfigById(@PathVariable String categoryId) {
        CompatibleOptionEditResponse result = categoryConfigService.getCategoryAndCompatibleOption(UUID.fromString(categoryId));
        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/category/config/{categoryId}")
    public ResponseEntity<GenericResponse> update(@RequestBody CompatibleOptionEditResponse compatibleOptionEditResponse) {
        GenericResponse result = categoryConfigService.updateCategoryAndConfigs(compatibleOptionEditResponse);
        return ResponseEntity.ok(result);
    }
}

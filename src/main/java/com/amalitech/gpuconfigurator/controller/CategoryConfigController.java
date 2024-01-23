package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryListResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/v1/category/config")
    public ResponseEntity<List<CategoryListResponse>> getAllCategoryConfig() {
        List<CategoryListResponse> result = categoryConfigService.getCategoryListResponses();
        return ResponseEntity.ok(result);
    }
}

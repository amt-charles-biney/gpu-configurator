package com.amalitech.gpuconfigurator.controller.category;

import com.amalitech.gpuconfigurator.dto.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import com.amalitech.gpuconfigurator.service.category.CategoryConfig.CategoryConfigService;
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
    @PostMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<GenericResponse> addConfig(@PathVariable String categoryId, @RequestBody List<CompatibleOptionDTO> categoryConfigDto) {
        GenericResponse result = categoryConfigService.createCategoryConfig(UUID.fromString(categoryId), categoryConfigDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<CategoryConfigResponseDto> getConfigs(@PathVariable String categoryId) {
        CategoryConfigResponseDto result = categoryConfigService.getCategoryConfigByCategory(categoryId);
        return ResponseEntity.ok(result);
    }
}

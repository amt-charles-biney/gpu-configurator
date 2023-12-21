package com.amalitech.gpuconfigurator.controller.category;

import com.amalitech.gpuconfigurator.dto.CategoryConfigDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import com.amalitech.gpuconfigurator.service.category.CategoryConfig.CategoryConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryConfigController {

    private final CategoryConfigService categoryConfigService;

    @PostMapping("/v1/admin/category/{categoryId}/config")
    public ResponseEntity<GenericResponse> createConfig(@PathVariable String categoryId, @RequestBody CategoryConfigDto categoryConfigDto) {
        GenericResponse config = categoryConfigService.createCategoryConfig(UUID.fromString(categoryId), categoryConfigDto);
        return ResponseEntity.ok(config);
    }


}

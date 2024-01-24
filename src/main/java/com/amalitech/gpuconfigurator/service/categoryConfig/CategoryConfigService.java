package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryListResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionEditResponse;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CategoryConfigService {
    @Transactional
    GenericResponse createCategoryConfig(CategoryConfigRequest request);

    CategoryConfig getCategoryConfig(String id);

    CategoryConfigResponseDto getCategoryConfigByCategory(String id);

    List<CategoryListResponse> getCategoryListResponses();

    CompatibleOptionEditResponse getCategoryAndCompatibleOption(UUID categoryId);

    @Transactional
    GenericResponse deleteCategoryAndCategoryConfig(List<String> categoryIds);

    @Transactional
    GenericResponse updateCategoryAndConfigs(CompatibleOptionEditResponse compatibleOptionEditResponse);
}

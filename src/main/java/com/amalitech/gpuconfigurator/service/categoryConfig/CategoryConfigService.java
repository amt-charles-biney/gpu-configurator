package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CategoryConfigService {
    @Transactional
    GenericResponse createCategoryConfig(CategoryConfigRequest request);

    CategoryConfig getCategoryConfig(String id);

    CategoryConfigResponseDto getCategoryConfigByCategory(String id);

    Page<CategoryListResponse> getCategoryListResponsesPageable(int size, int page, String query);

    List<CategoryListResponse> getCategoryListResponses();

    CompatibleOptionGetResponse getCategoryAndCompatibleOption(UUID categoryId);

    @Transactional
    GenericResponse deleteCategoryAndCategoryConfig(List<String> categoryIds);

    @Transactional
    CompatibleOptionGetResponse updateCategoryAndConfigs(CompatibleOptionEditResponse compatibleOptionEditResponse);

    @Transactional
    void updateExistingCategoryConfigs(List<AttributeOption> attributeOptions);
}

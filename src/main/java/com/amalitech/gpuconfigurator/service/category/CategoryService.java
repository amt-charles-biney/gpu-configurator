package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(CategoryRequestDto request);

    Category getCategory(String categoryName);

    CategoryResponse updateCategory(String categoryId, @NotNull  String name, String thumbnail, List<String> caseIds);

    List<AllCategoryResponse> getAllCategories();

    CategoryResponse getCategoryByName(String name);

    void deleteAllById(List<UUID> categoryIds);
}

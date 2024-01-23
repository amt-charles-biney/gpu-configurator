package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequestDto request) throws DataIntegrityViolationException;

    Category getCategory(String categoryName);

    CategoryResponse updateCategory(String categoryId, @NotNull String name);

    List<AllCategoryResponse> getAllCategories();

    Category getCategoryByName(String name);
}

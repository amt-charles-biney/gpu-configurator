package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.category.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(CategoryRequestDto request);
    public Category getCategory(String categoryName);

    List<AllCategoryResponse> getAllCategories();

    List<Category> getCategoryByName(String name);
}

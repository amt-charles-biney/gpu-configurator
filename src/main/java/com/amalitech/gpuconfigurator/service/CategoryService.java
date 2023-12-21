package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public Category createCategory(CategoryRequestDto request);
    public Category getCategory(String categoryName);

    List<AllCategoryResponse> getAllCategories();

    List<Category> getCategoryByName(String name);
}

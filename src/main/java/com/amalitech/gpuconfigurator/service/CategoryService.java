package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(CategoryRequestDto request);
    public Category getCategory(String categoryName);

    List<AllCategoryResponse> getAllCategories();

    Category getCategoryByName(String name);
}

package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.category.Category;

import java.util.List;

public interface CategoryService {
    public Category addCategory(CategoryRequestDto request);
    public Category getCategory(String categoryName);

    List<Category> getAllCategories();
}

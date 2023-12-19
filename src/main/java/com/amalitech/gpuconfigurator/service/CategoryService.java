package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.category.Category;

public interface CategoryService {
    public Category addCategory(CategoryRequestDto request);
    public Category getOrCreateCategory(String categoryName);
}

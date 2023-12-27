package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;

import java.util.List;

public interface CategoryService {
     Category createCategory(CategoryRequestDto request);
     Category getCategory(String categoryName);

    List<AllCategoryResponse> getAllCategories();

    Category getCategoryByName(String name);
}

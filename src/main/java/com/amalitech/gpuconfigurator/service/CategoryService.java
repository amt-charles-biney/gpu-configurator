package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.Role;

import java.util.List;

public interface CategoryService {
    public Role.Category createCategory(CategoryRequestDto request);
    public Role.Category getCategory(String categoryName);

    List<AllCategoryResponse> getAllCategories();

    List<Role.Category> getCategoryByName(String name);
}

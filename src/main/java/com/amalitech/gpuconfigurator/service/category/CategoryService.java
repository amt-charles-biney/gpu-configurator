package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;
import com.cloudinary.api.exceptions.BadRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(CategoryRequestDto request) throws DataIntegrityViolationException, BadRequest;

    Category getCategory(String categoryName);

    CategoryResponse updateCategory(String categoryId, @NotNull String name);

    List<AllCategoryResponse> getAllCategories();

    Category getCategoryByName(String name);

    void deleteAllById(List<UUID> categoryIds);
}

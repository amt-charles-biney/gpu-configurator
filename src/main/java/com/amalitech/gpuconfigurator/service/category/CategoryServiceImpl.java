package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.cloudinary.api.exceptions.BadRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequestDto request) throws BadRequest {
        if(categoryRepository.existsByCategoryName(request.name())) throw new BadRequest("category already exists");

        var category = Category
                .builder()
                .categoryName(request.name())
                .build();

        return categoryRepository.save(category);
    }


    @Override
    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new NotFoundException("Category not found"));
    }

    @Override
    public CategoryResponse updateCategory(String categoryId, String name) {
        Category category = categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(() -> new EntityNotFoundException("category not found"));

        category.setCategoryName(name);
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse
                .builder()
                .name(savedCategory.getCategoryName())
                .id(savedCategory.getId().toString())
                .build();

    }

    @Override
    public List<AllCategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        return allCategories.stream()
                .map(category -> AllCategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getCategoryName()).build())
                .toList();

    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name).orElseThrow(()-> new NotFoundException(name+ " "+ "Not found"));
    }

    @Override
    public void deleteAllById(List<UUID> categoryIds) {
        categoryRepository.deleteAllById(categoryIds);
    }
}


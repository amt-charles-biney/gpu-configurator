package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryRequestDto request) {
        var category = Category.builder().categoryName(request.name()).build();
        return categoryRepository.save(category);
    }


    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new EntityNotFoundException("Category not found"));
    }

    @Override
    public List<AllCategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        return allCategories.stream()
                .map(category -> AllCategoryResponse.builder().categoryName(category.getCategoryName()).id(category.getId()).build())
                .toList();

    }

    @Override
    public List<Category> getCategoryByName(String name) {
        var categoryList = categoryRepository.findByCategoryNameList(name);

        if(categoryList == null){
            return Collections.emptyList();
        }

        return categoryList;

    }


}


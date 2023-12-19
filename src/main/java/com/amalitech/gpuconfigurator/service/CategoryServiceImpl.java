package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category addCategory(CategoryRequestDto request){
        var category = Category.builder().categoryName(request.name()).build();
        categoryRepository.save(category);

        return Category.builder()
                .categoryName(category.getCategoryName())
                .build();

    }
    public Category getOrCreateCategory(String categoryName) {
    Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
    return category.orElseGet(() -> addCategory(new CategoryRequestDto(categoryName)));
 }

}


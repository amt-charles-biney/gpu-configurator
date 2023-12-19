package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category addCategory(CategoryRequestDto request){
        var category = Category.builder().categoryName(request.name()).build();
        return categoryRepository.save(category);

    }
    public Category getCategory(String categoryName) {
    var category = categoryRepository.findByCategoryName(categoryName);

    if(category == null){
        throw new EntityNotFoundException();
    }

    return category;
 }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}


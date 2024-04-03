package com.amalitech.gpuconfigurator.service.category;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Case;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.service.cases.CaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CaseServiceImpl caseService;

    @Override
    public Category createCategory(CategoryRequestDto request) {
        if (categoryRepository.existsByCategoryName(request.name()))
            throw new AttributeNameAlreadyExistsException("category already exists");

        List<Case> cases = getCategoryCases(request.caseIds());
        Category category = Category
                .builder()
                .categoryName(request.name())
                .thumbnail(request.thumbnail())
                .build();

        category.addCases(cases);
        return categoryRepository.save(category);
    }


    @Override
    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public CategoryResponse updateCategory(String categoryId, String name, String thumbnail, List<String> caseIds) {
        Category category = categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(() -> new EntityNotFoundException("category not found"));

        List<Case> cases = getCategoryCases(caseIds);

        category.setCategoryName(name);
        category.setThumbnail(thumbnail);
        category.addCases(cases);

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

        return allCategories.stream().filter(category -> !"unassigned".equals(category.getCategoryName()))
                .map(category -> AllCategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getCategoryName())
                        .thumbnail(category.getThumbnail())
                        .build())
                .toList();
    }

    @Override
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByCategoryName(name).orElseThrow(() -> new NotFoundException(name + " " + "Not found"));
        return new CategoryResponse(category.getId().toString(), category.getCategoryName(), category.getThumbnail());

    }

    @Override
    public void deleteAllById(List<UUID> categoryIds) {
        categoryRepository.deleteAllById(categoryIds);
    }

    public List<Case> getCategoryCases(List<String> caseIds) {
        List<UUID> caseUUIDs = caseIds.stream().map(UUID::fromString).toList();
        return caseService.findAllCasesById(caseUUIDs);
    }

    public List<CaseResponse> getAllCaseByCategoryConfig(CategoryConfig categoryConfig) {
          return categoryConfig.getCategory().getCategoryCase().stream().map(
                        productCase -> this.caseService.mapCaseToCaseResponse(productCase))
                .toList();
    }
}


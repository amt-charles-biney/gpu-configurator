package com.amalitech.gpuconfigurator.service.category.CategoryConfig;

import com.amalitech.gpuconfigurator.dto.CategoryConfigDto;
import com.amalitech.gpuconfigurator.dto.CategoryConfigOptionDto;
import com.amalitech.gpuconfigurator.dto.CompatibleOptionDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import com.amalitech.gpuconfigurator.model.category.CategoryConfigOption;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.category.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.category.CategoryRepository;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryConfigService {

    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryConfigOptionService categoryConfigOptionService;
    private final CompatibleOptionService compatibleOptionService;

    @Transactional
    public GenericResponse createCategoryConfig(UUID id, CategoryConfigDto categoryConfigDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category not found"));

        CategoryConfig config = CategoryConfig
                .builder()
                .category(category)
                .build();

        CategoryConfig savedCategoryConfig = categoryConfigRepository.save(config);

        List<CategoryConfigOptionDto> categoryConfigOptionRequestToResponse = categoryConfigDto
                .categoryConfigOptions()
                .stream()
                .map(option -> CategoryConfigOptionDto
                        .builder()
                        .categoryConfig(savedCategoryConfig)
                        .attribute(option.attribute())
                        .attributeOption(option.attributeOption())
                        .category(category)
                        .build()
                ).toList();

        List<CompatibleOptionDto> compatibleOptionRequestToResponse = categoryConfigDto.compatibleOptions()
                .stream()
                .map(option -> CompatibleOptionDto
                        .builder()
                        .isCompatible(option.isCompatible())
                        .attributeOption(option.attributeOption())
                        .attribute(option.attribute())
                        .categoryConfig(savedCategoryConfig)
                        .build()).toList();


        categoryConfigOptionService.addBulkCategoryConfigOptions(categoryConfigOptionRequestToResponse);
        compatibleOptionService.addBulkCompatibleOptions(compatibleOptionRequestToResponse);

        return new GenericResponse(201, "category config created successfully " +  config.getId());
    }

    public CategoryConfig getCategoryConfig(String id) {
        return categoryConfigRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    public CategoryConfig getCategoryConfigByCategory(String id) {
        return categoryConfigRepository.findByCategoryId(UUID.fromString(id));
    }
}

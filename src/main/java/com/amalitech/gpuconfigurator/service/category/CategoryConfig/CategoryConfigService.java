package com.amalitech.gpuconfigurator.service.category.CategoryConfig;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.category.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.category.CategoryRepository;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryConfigService {

    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryRepository categoryRepository;
    private final CompatibleOptionService compatibleOptionService;

    @Transactional
    public GenericResponse createCategoryConfig(UUID id, List<CompatibleOptionDTO> compatibleOptions) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category not found"));

        CategoryConfig config = CategoryConfig
                .builder()
                .category(category)
                .build();

        CategoryConfig savedCategoryConfig = categoryConfigRepository.save(config);

        List<CompatibleOption> compatibleOptionRequestToResponse = compatibleOptions
                .stream()
                .map(option -> CompatibleOption
                        .builder()
                        .type(option.type())
                        .price(option.price())
                        .name(option.name())
                        .media(option.media())
                        .isCompatible(option.isCompatible())
                        .unit(option.unit())
                        .isIncluded(option.isIncluded())
                        .categoryConfig(savedCategoryConfig)
                        .build()).toList();

        compatibleOptionService.addBulkCompatibleOptions(compatibleOptionRequestToResponse);
        return new GenericResponse(201, "category config created successfully " + config.getId());
    }

    public CategoryConfig getCategoryConfig(String id) {
        return categoryConfigRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    public CategoryConfigResponseDto getCategoryConfigByCategory(String id) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(id));

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        AttributeResponseDto categoryResponse = AttributeResponseDto.builder().id(categoryConfig.getCategory().getId().toString()).name(categoryConfig.getCategory().getCategoryName()).build();

        List<CompatibleOptionResponseDto> compatibleOptionResponse = compatibleOptions.stream()
                .map(option -> CompatibleOptionResponseDto.builder()
                        .id(option.getId().toString())
                        .type(option.getType())
                        .price(option.getPrice())
                        .name(option.getName())
                        .media(option.getMedia())
                        .isCompatible(option.getIsCompatible())
                        .unit(option.getUnit())
                        .isIncluded(option.getIsIncluded())
                        .build()
                ).toList();

        return CategoryConfigResponseDto.builder()
                .id(categoryConfig.getId().toString())
                .category(categoryResponse)
                .options(compatibleOptionResponse)
                .build();
    }
}

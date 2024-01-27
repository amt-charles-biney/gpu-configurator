package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryConfigServiceImpl implements CategoryConfigService {

    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryServiceImpl categoryService;
    private final CompatibleOptionServiceImpl compatibleOptionServiceImpl;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public GenericResponse createCategoryConfig(CategoryConfigRequest request) {
        Category category = categoryService.createCategory(new CategoryRequestDto(request.name()));

        CategoryConfig config = CategoryConfig
                .builder()
                .category(category)
                .build();

        CategoryConfig savedCategoryConfig = categoryConfigRepository.save(config);

        List<CompatibleOption> compatibleOptionRequestToResponse = request.config()
                .stream()
                .map(option -> CompatibleOption
                        .builder()
                        .type(option.type())
                        .price(option.price())
                        .name(option.name())
                        .media(option.media())
                        .isMeasured(option.isMeasured())
                        .priceFactor(option.priceFactor())
                        .baseAmount(option.baseAmount())
                        .maxAmount(option.maxAmount())
                        .isCompatible(option.isCompatible())
                        .unit(option.unit())
                        .isIncluded(option.isIncluded())
                        .categoryConfig(savedCategoryConfig)
                        .build()).toList();

        compatibleOptionServiceImpl.addBulkCompatibleOptions(compatibleOptionRequestToResponse);
        return new GenericResponse(201, "category config created successfully " + config.getId());
    }

    @Override
    public CategoryConfig getCategoryConfig(String id) {
        return categoryConfigRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    @Override
    public CategoryConfigResponseDto getCategoryConfigByCategory(String id) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("config does not exist"));
        List<CompatibleOption> compatibleOptions = compatibleOptionServiceImpl.getByCategoryConfigId(categoryConfig.getId());

        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(categoryConfig.getCategory().getId().toString())
                .name(categoryConfig.getCategory().getCategoryName())
                .build();

        Map<String, List<CompatibleOptionResponseDto>> compatibleGroupedByType = compatibleOptions.stream()
                .collect(Collectors.groupingBy(CompatibleOption::getType,                           // Group by 'type'
                        Collectors.mapping(option -> CompatibleOptionResponseDto.builder()      // Map each group to a list of CompatibleOptionResponseDto
                                        .id(option.getId().toString())
                                        .type(option.getType())
                                        .price(option.getPrice())
                                        .name(option.getName())
                                        .media(option.getMedia())
                                        .isCompatible(option.getIsCompatible())
                                        .maxAmount(option.getMaxAmount())
                                        .priceFactor(option.getPriceFactor())
                                        .isMeasured(option.getIsMeasured())
                                        .baseAmount(option.getBaseAmount())
                                        .unit(option.getUnit())
                                        .isIncluded(option.getIsIncluded())
                                        .build(),
                                Collectors.toList())));

        return CategoryConfigResponseDto.builder()
                .id(categoryConfig.getId().toString())
                .category(categoryResponse)
                .options(compatibleGroupedByType)
                .build();
    }

    @Override
    public List<CategoryListResponse> getCategoryListResponses() {
        List<CategoryConfig> categoryConfigs = categoryConfigRepository.findAll();

        return categoryConfigs.stream().map(
                config -> CategoryListResponse.builder()
                        .id(config.getCategory().getId().toString())
                        .name(config.getCategory().getCategoryName())
                        .config(this.extractAttributesFromCompatibleOptions(config.getId()))
                        .productCount(this.extractProductCount(config.getCategory().getId()))
                        .build()
        ).toList();
    }

    @Override
    public CompatibleOptionGetResponse getCategoryAndCompatibleOption(UUID categoryId) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(categoryId).orElseThrow(() -> new EntityNotFoundException("config category not found"));

        List<CompatibleOption> compatibleOptions = compatibleOptionServiceImpl.getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId());

        List<CompatibleOptionResponseDto> compatibleOptionResponseDtoList = compatibleOptions
                .stream()
                .map(compatibleOption -> CompatibleOptionResponseDto
                .builder()
                        .id(compatibleOption.getId().toString())
                        .isCompatible(compatibleOption.getIsCompatible())
                        .isIncluded(compatibleOption.getIsIncluded())
                        .priceIncrement(compatibleOption.getPriceIncrement())
                        .priceFactor(compatibleOption.getPriceFactor())
                        .unit(compatibleOption.getUnit())
                        .name(compatibleOption.getName())
                        .type(compatibleOption.getType())
                        .maxAmount(compatibleOption.getMaxAmount())
                        .price(compatibleOption.getPrice())
                        .media(compatibleOption.getMedia())
                        .baseAmount(compatibleOption.getBaseAmount())
                        .isMeasured(compatibleOption.getIsMeasured())
                .build()).toList();

        return CompatibleOptionGetResponse.builder()
                .name(categoryConfig.getCategory().getCategoryName())
                .id(categoryConfig.getCategory().getId().toString())
                .config(compatibleOptionResponseDtoList)
                .build();
    }

    @Override
    @Transactional
    public GenericResponse deleteCategoryAndCategoryConfig(List<String> categoryIds) {
        List<UUID> categoryUUIDs = categoryIds.stream().map(UUID::fromString).toList();

        categoryConfigRepository.deleteAllByCategoryId(categoryUUIDs);
        categoryService.deleteAllById(categoryUUIDs);

        return new GenericResponse(HttpStatus.ACCEPTED.value(), "deleted category successfully");

    }

    @Override
    @Transactional
    public GenericResponse updateCategoryAndConfigs(CompatibleOptionEditResponse compatibleOptionEditResponse) {
        categoryService.updateCategory(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name());
        compatibleOptionServiceImpl.updateBulkCompatibleOptions(compatibleOptionEditResponse.config());

        return new GenericResponse(HttpStatus.ACCEPTED.value(), "updated category and config");
    }


    public List<String> extractAttributesFromCompatibleOptions(UUID categoryConfigId) {
        List<CompatibleOption> compatibleOptions = compatibleOptionServiceImpl.getAllCompatibleOptionsByCategoryConfig(categoryConfigId);
        List<String> uniqueTypes = compatibleOptions.stream()
                .map(CompatibleOption::getType)
                .distinct()
                .collect(Collectors.toList());

        return uniqueTypes;
    }

    public Long extractProductCount(UUID category) {
        return productRepository.countProductsByCategoryId(category);
    }
}

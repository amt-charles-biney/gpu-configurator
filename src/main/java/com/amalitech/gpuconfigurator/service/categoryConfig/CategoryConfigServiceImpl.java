package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
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
    private final CompatibleOptionServiceImpl compatibleOptionService;
    private final ProductRepository productRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final CategoryRepository categoryRepository;

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
                        .isCompatible(option.isCompatible())
                        .isIncluded(option.isIncluded())
                        .attributeOption(this.getAttributeOption(option.attributeOptionId()))
                        .size(option.size())
                        .categoryConfig(savedCategoryConfig)
                        .build()).toList();

        compatibleOptionService.addBulkCompatibleOptions(compatibleOptionRequestToResponse);
        return new GenericResponse(201, "category config created successfully " + config.getId());
    }

    @Override
    public CategoryConfig getCategoryConfig(String id) {
        return categoryConfigRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    @Override
    public CategoryConfigResponseDto getCategoryConfigByCategory(String id) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("config does not exist"));
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(categoryConfig.getCategory().getId().toString())
                .name(categoryConfig.getCategory().getCategoryName())
                .build();

        Map<String, List<CompatibleOptionResponseDto>> compatibleGroupedByType = compatibleOptions.stream()
                .collect(Collectors.groupingBy((option) -> option.getAttributeOption().getAttribute().getAttributeName(),
                        Collectors.mapping(option -> CompatibleOptionResponseDto.builder()
                                        .compatibleOptionId(option.getId().toString())
                                        .type(option.getAttributeOption().getAttribute().getAttributeName())
                                        .price(option.getAttributeOption().getPriceAdjustment())
                                        .name(option.getAttributeOption().getOptionName())
                                        .media(option.getAttributeOption().getMedia())
                                        .isCompatible(option.getIsCompatible())
                                        .maxAmount(option.getAttributeOption().getMaxAmount())
                                        .priceFactor(option.getAttributeOption().getPriceFactor())
                                        .isMeasured(option.getIsMeasured())
                                        .baseAmount(option.getAttributeOption().getBaseAmount())
                                        .unit(option.getAttributeOption().getAttribute().getUnit())
                                        .isIncluded(option.getIsIncluded())
                                        .size(option.getSize())
                                        .attributeId(option.getAttributeOption().getAttribute().getId().toString())
                                        .attributeOptionId(option.getAttributeOption().getId().toString())
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

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId());

        List<CompatibleOptionResponseDto> compatibleOptionResponseDtoList = compatibleOptions
                .stream()
                .map(compatibleOption -> CompatibleOptionResponseDto
                .builder()
                        .compatibleOptionId(compatibleOption.getId().toString())
                        .isCompatible(compatibleOption.getIsCompatible())
                        .isIncluded(compatibleOption.getIsIncluded())
                        .priceIncrement(null)
                        .priceFactor(compatibleOption.getAttributeOption().getPriceFactor())
                        .unit(compatibleOption.getAttributeOption().getAttribute().getUnit())
                        .name(compatibleOption.getAttributeOption().getOptionName())
                        .type(compatibleOption.getAttributeOption().getAttribute().getAttributeName())
                        .maxAmount(compatibleOption.getAttributeOption().getMaxAmount())
                        .size(compatibleOption.getSize())
                        .price(compatibleOption.getAttributeOption().getPriceAdjustment())
                        .media(compatibleOption.getAttributeOption().getMedia())
                        .baseAmount(compatibleOption.getAttributeOption().getBaseAmount())
                        .isMeasured(compatibleOption.getAttributeOption().getAttribute().isMeasured())
                        .attributeId(compatibleOption.getAttributeOption().getAttribute().getId().toString())
                        .attributeOptionId(compatibleOption.getAttributeOption().getId().toString())
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
        compatibleOptionService.updateBulkCompatibleOptions(compatibleOptionEditResponse.config());

        return new GenericResponse(HttpStatus.ACCEPTED.value(), "updated category and config");
    }


    public List<String> extractAttributesFromCompatibleOptions(UUID categoryConfigId) {
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getAllCompatibleOptionsByCategoryConfig(categoryConfigId);

        return compatibleOptions.stream()
                .map(option -> option.getAttributeOption().getAttribute().getAttributeName())
                .distinct()
                .collect(Collectors.toList());
    }

    public Long extractProductCount(UUID category) {
        return productRepository.countProductsByCategoryId(category);
    }
    public AttributeOption getAttributeOption (String id) {
        return attributeOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not find attribute option"));
    }
}

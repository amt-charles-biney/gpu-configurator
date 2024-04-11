package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.cases.CaseServiceImpl;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        Category category = categoryService.createCategory(new CategoryRequestDto(request.name(), request.thumbnail(), request.caseIds()));

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
        return categoryConfigRepository.findByCategoryId(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    @Override
    public CategoryConfigResponseDto getCategoryConfigByCategory(String id) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("config does not exist"));
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId()).stream().filter(option -> option.getIsCompatible() || option.getIsIncluded()).toList();

        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(categoryConfig.getCategory().getId().toString())
                .name(categoryConfig.getCategory().getCategoryName())
                .thumbnail(categoryConfig.getCategory().getThumbnail())
                .build();

        Map<String, List<CompatibleOptionResponseDto>> compatibleGroupedByType = mapToCompatibleOptionResponseDtoMap(compatibleOptions)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().stream().anyMatch(option -> option.isCompatible() || option.isIncluded()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<CaseResponse> caseResponses = categoryService.getAllCaseByCategoryConfig(categoryConfig);

        List<VariantStockLeastDto> totalLeastStock = getTotalLeastStocks(compatibleOptions);
        int inStock = getTotalLeastStock(compatibleOptions);

        return CategoryConfigResponseDto.builder()
                .id(categoryConfig.getId().toString())
                .category(categoryResponse)
                .options(compatibleGroupedByType)
                .inStock(inStock)
                .totalLeastStocks(totalLeastStock)
                .cases(caseResponses)
                .build();
    }

    @Override
    public Page<CategoryListResponse> getCategoryListResponsesPageable(int size, int page, String query) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryRepository.findAllByCategoryNameContainingIgnoreCaseAndCategoryNameNot(query,"unassigned",pageRequest)
                .map(category -> CategoryListResponse.builder()
                        .id(category.getId().toString())
                        .name(category.getCategoryName())
                        .config(this.extractAttributesFromCompatibleOptions(category.getCategoryConfig().getId()))
                        .productCount(this.extractProductCount(category.getId()))
                        .build());

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
                        .inStock(compatibleOption.getAttributeOption().getInStock())
                        .baseAmount(compatibleOption.getAttributeOption().getBaseAmount())
                        .isMeasured(compatibleOption.getAttributeOption().getAttribute().isMeasured())
                        .attributeId(compatibleOption.getAttributeOption().getAttribute().getId().toString())
                        .attributeOptionId(compatibleOption.getAttributeOption().getId().toString())
                        .build()).toList();

        List<CaseResponse> caseResponses = categoryService.getAllCaseByCategoryConfig(categoryConfig);

        double totalPrice = compatibleOptionResponseDtoList
                .stream()
                .mapToDouble(compatibleOption -> compatibleOption.price() != null ? compatibleOption.price().doubleValue() : 0.0)
                .sum();

        List<VariantStockLeastDto> leastStocks = getTotalLeastStocks(compatibleOptions);

        int lowestInStock = getTotalLeastStock(compatibleOptions);

        return CompatibleOptionGetResponse.builder()
                .name(categoryConfig.getCategory().getCategoryName())
                .thumbnail(categoryConfig.getCategory().getThumbnail())
                .id(categoryConfig.getCategory().getId().toString())
                .config(compatibleOptionResponseDtoList)
                .inStock(lowestInStock)
                .totalLeastStocks(leastStocks)
                .configPrice(totalPrice)
                .cases(caseResponses)
                .build();
    }

    @Override
    @Transactional
    public GenericResponse deleteCategoryAndCategoryConfig(List<String> categoryIds) {
        List<UUID> categoryUUIDs = categoryIds.stream().map(UUID::fromString).toList();

        List<Product> products = productRepository.findProductsByCategoryIds(categoryUUIDs);

        var unassignedCategory = categoryRepository.findByCategoryName("unassigned").orElse(Category.builder().categoryName("unassigned").build());

        for (var product : products) {
            product.setCategory(unassignedCategory);
        }

        categoryService.deleteAllById(categoryUUIDs);
        return new GenericResponse(HttpStatus.ACCEPTED.value(), "deleted category successfully");
    }

    @Override
    @Transactional
    public GenericResponse updateCategoryAndConfigs(CompatibleOptionEditResponse compatibleOptionEditResponse) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(compatibleOptionEditResponse.id()))
                .orElseThrow(() -> new EntityNotFoundException("configuration does not exist"));

        categoryService.updateCategory(
                compatibleOptionEditResponse.id(),
                compatibleOptionEditResponse.name(),
                compatibleOptionEditResponse.thumbnail(),
                compatibleOptionEditResponse.caseIds()
        );
        compatibleOptionService.updateBulkCompatibleOptions(categoryConfig, compatibleOptionEditResponse.config());

        return new GenericResponse(HttpStatus.ACCEPTED.value(), "updated category and config");
    }

    public CategoryConfigResponseDto getCategoryConfigByProduct(String productId) {
        Product product = productRepository.findById(UUID.fromString(productId)).orElseThrow(() -> new EntityNotFoundException("product not found"));
        List<AttributeOption> caseIncompatibleAttributes = product.getProductCase().getIncompatibleVariants();

        List<CompatibleOption> compatibleOptions = getAllCompatibleOptionsByCategoryId(product.getCategory().getId())
                .stream()
                .filter(option -> option.getIsCompatible() || option.getIsIncluded())
                .filter(option -> caseIncompatibleAttributes.stream()
                        .noneMatch(attribute -> attribute.getId().equals(option.getAttributeOption().getId())))
                .toList();

        Map<String, List<CompatibleOptionResponseDto>> mapCompatibleOptionResponse = mapToCompatibleOptionResponseDtoMap(compatibleOptions);

        return CategoryConfigResponseDto
                .builder()
                .options(mapCompatibleOptionResponse)
                .build();
    }


    public Map<String, List<CategoryConfigDisplay>> extractAttributesFromCompatibleOptions(UUID categoryConfigId) {
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getAllCompatibleOptionsByCategoryConfig(categoryConfigId);

        return mapToCompatibleOptionResponseDtoMap(compatibleOptions)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().stream().anyMatch(option -> option.isCompatible() || option.isIncluded()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(option -> new CategoryConfigDisplay(option.name(), option.isIncluded()))
                                .collect(Collectors.toList())
                ));

    }

    public List<CompatibleOption> getAllCompatibleOptionsByCategoryId(UUID categoryId) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(categoryId).orElseThrow(() -> new EntityNotFoundException("config category not found"));
        return compatibleOptionService.getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId());
    }

    @Override
    @Transactional
    public void updateExistingCategoryConfigs(List<AttributeOption> attributeOptions) {
        List<CategoryConfig> categoryConfigs = categoryConfigRepository.findAll();

        for (CategoryConfig categoryConfig : categoryConfigs) {
            List<CompatibleOption> getCategoryCompatibleOptions = categoryConfig.getCompatibleOptions();

            List<CompatibleUpdateDto> compatibleUpdateDtos =
                    getNewDistinctAttributeOptions(attributeOptions, getCategoryCompatibleOptions).stream()
                            .map(option -> CompatibleUpdateDto.builder()
                                    .attributeId(option.getAttribute().getId().toString())
                                    .attributeOptionId(option.getId().toString())
                                    .size(option.getBaseAmount() != null ? Math.round(option.getBaseAmount()) : 0)
                                    .isCompatible(true)
                                    .isIncluded(false)
                                    .isMeasured(option.getAttribute().isMeasured())
                                    .build())
                            .toList();

            compatibleOptionService.updateBulkCompatibleOptions(categoryConfig, compatibleUpdateDtos);
        }

        GenericResponse.builder()
                .status(201)
                .message("bulk update attribute compatible options")
                .build();
    }

    public List<AttributeOption> getNewDistinctAttributeOptions(@NotNull List<AttributeOption> attributeOptions, List<CompatibleOption> compatibleOptions) {
        return attributeOptions.stream()
                .filter(option -> compatibleOptions.stream()
                        .noneMatch(existingOption -> existingOption.getAttributeOption().getId().equals(option.getId())))
                .toList();
    }

    private Map<String, List<CompatibleOptionResponseDto>> mapToCompatibleOptionResponseDtoMap(List<CompatibleOption> compatibleOptions) {

        return compatibleOptions.stream()
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
                                        .isMeasured(option.getAttributeOption().getAttribute().isMeasured())
                                        .baseAmount(option.getAttributeOption().getBaseAmount())
                                        .unit(option.getAttributeOption().getAttribute().getUnit())
                                        .isIncluded(option.getIsIncluded())
                                        .inStock(option.getAttributeOption().getInStock())
                                        .size(option.getSize())
                                        .attributeId(option.getAttributeOption().getAttribute().getId().toString())
                                        .attributeOptionId(option.getAttributeOption().getId().toString())
                                        .build(),
                                Collectors.toList())));

    }

    public Long extractProductCount(UUID category) {
        return productRepository.countProductsByCategoryIdAndIsDeletedFalse(category);
    }

    public AttributeOption getAttributeOption(String id) {
        return attributeOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not find attribute option"));
    }

    public List<VariantStockLeastDto> getTotalLeastStocks(List<CompatibleOption> compatibleOptions) {
        return compatibleOptions.stream()
                .filter(compatibleOption -> compatibleOption.getIsIncluded())
                .filter(co -> co.getAttributeOption() != null && co.getAttributeOption().getInStock() != null && co.getAttributeOption().getInStock() <= 5)
                .map(co -> {
                    AttributeOption attributeOption = co.getAttributeOption();
                    return new VariantStockLeastDto(attributeOption.getOptionName(), attributeOption.getAttribute().getId().toString(), attributeOption.getInStock());
                })
                .collect(Collectors.toList());
    }

    public List<VariantStockLeastDto> getTotalLeastStocksInclusive(List<CompatibleOption> compatibleOptions) {
        return compatibleOptions.stream()
                .filter(co -> co.getAttributeOption() != null && co.getAttributeOption().getInStock() != null && co.getAttributeOption().getInStock() <= 5)
                .map(co -> {
                    AttributeOption attributeOption = co.getAttributeOption();
                    return new VariantStockLeastDto(attributeOption.getOptionName(), attributeOption.getAttribute().getId().toString(), attributeOption.getInStock());
                })
                .collect(Collectors.toList());
    }

    public int getTotalLeastStock(List<CompatibleOption> compatibleOptions) {
        Optional<Integer> minStock = compatibleOptions.stream()
                .filter(compatibleOption -> compatibleOption.getIsIncluded())
                .map(co -> co.getAttributeOption().getInStock())
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder());

        return minStock.orElse(0);
    }
}

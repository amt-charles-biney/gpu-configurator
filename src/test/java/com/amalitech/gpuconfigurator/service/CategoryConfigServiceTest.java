package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryConfigServiceTest {

    @Mock
    private CategoryConfigRepository categoryConfigRepository;
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private CompatibleOptionServiceImpl compatibleOptionServiceImpl;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CategoryConfigServiceImpl categoryConfigService;

    private Category category;
    private CategoryConfig categoryConfig;
    private CategoryConfigRequest categoryConfigRequest;
    private CompatibleOption compatibleOption;
    private AttributeOption attributeOption;
    private CompatibleOptionGetResponse compatibleOptionGetResponse;

    private Attribute attribute;

    private CompatibleOptionEditResponse compatibleOptionEditResponse;

    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    @Mock
    private CompatibleOptionService compatibleOptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        attribute = Attribute.builder()
                .unit("SampleUnit")
                .isMeasured(true)
                .description("")
                .attributeName("DISK")
                .id(UUID.randomUUID())
                .build();

        CompatibleOptionDTO compatibleOptionDTO = CompatibleOptionDTO.builder()
                .attributeOptionId(UUID.randomUUID().toString())
                .isCompatible(true)
                .isIncluded(false)
                .isMeasured(true)
                .size(42)
                .attributeId("yourAttributeId")
                .build();

        attributeOption = AttributeOption.builder()
                .optionName("SampleOption")
                .priceAdjustment(BigDecimal.valueOf(25.0))
                .inStock(0)
                .attribute(attribute)
                .id(UUID.randomUUID())
                .baseAmount(10.0F)
                .maxAmount(50.0F)
                .priceFactor(1.2)
                .build();


        categoryConfigRequest = new CategoryConfigRequest("TestCategory", "./hello_world.jpg",
                Collections.singletonList(compatibleOptionDTO));

        category = Category.builder().id(UUID.randomUUID()).thumbnail("./hello_world.jpg").categoryName("TestCategory").build();

        categoryConfig = CategoryConfig.builder()
                .id(UUID.randomUUID())
                .category(category)
                .compatibleOptions(new ArrayList<>())
                .build();

        compatibleOption = CompatibleOption.builder()
                .id(UUID.randomUUID())
                .categoryConfig(categoryConfig)
                .attributeOption(attributeOption)
                .isMeasured(true)
                .isCompatible(true)
                .isIncluded(true)
                .size(25)
                .createdAt(LocalDateTime.now())
                .build();

        compatibleOptionGetResponse = CompatibleOptionGetResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("SampleCategory")
                .thumbnail("./hello_world.jpg")
                .config(Arrays.asList(
                        CompatibleOptionResponseDto.builder()
                                .compatibleOptionId(UUID.randomUUID().toString())
                                .attributeId(attributeOption.getAttribute().getId().toString())
                                .attributeOptionId(attributeOption.getId().toString())
                                .size(29)
                                .name("Option1")
                                .type("Type1")
                                .price(BigDecimal.valueOf(19.99))
                                .media("Media1")
                                .unit("Unit1")
                                .isCompatible(true)
                                .isIncluded(true)
                                .isMeasured(false)
                                .priceFactor(1.2)
                                .priceIncrement(0.1f)
                                .baseAmount(10.0f)
                                .maxAmount(20.0f)
                                .build()
                ))
                .build();

        CompatibleUpdateDto compatibleUpdateDto = CompatibleUpdateDto.builder()
                .attributeId(attributeOption.getAttribute().getId().toString())
                .compatibleOptionId(UUID.randomUUID().toString())
                .attributeOptionId(attributeOption.getId().toString())
                .isMeasured(true)
                .isIncluded(false)
                .isCompatible(true)
                .size(42)
                .build();

        compatibleOptionEditResponse = CompatibleOptionEditResponse.builder()
                .name("edit")
                .id(UUID.randomUUID().toString())
                .thumbnail("./hello_world.jpg")
                .config(List.of(compatibleUpdateDto))
                .build();

    }


    @Test
    void testCreateCategoryConfig() {

        when(categoryService.createCategory(any())).thenReturn(category);
        when(categoryConfigRepository.save(any())).thenReturn(categoryConfig);
        when(attributeOptionRepository.findById(any(UUID.class))).thenReturn(Optional.of(new AttributeOption()));


        GenericResponse response = categoryConfigService.createCategoryConfig(categoryConfigRequest);

        assertEquals(201, response.status());
        verify(categoryService, times(1)).createCategory(any());
        verify(categoryConfigRepository, times(1)).save(any());
        verify(compatibleOptionServiceImpl, times(1)).addBulkCompatibleOptions(anyList());
    }

    @Test
    void getCategoryConfig() {
        UUID categoryId = UUID.randomUUID();
        String categoryIdString = categoryId.toString();
        compatibleOption.setId(UUID.randomUUID());
        when(categoryConfigRepository.findByCategoryId(categoryId)).thenReturn(Optional.ofNullable(categoryConfig));

        when(compatibleOptionServiceImpl.getByCategoryConfigId(categoryConfig.getId())).thenReturn(List.of(compatibleOption));

        CategoryConfigResponseDto response = categoryConfigService.getCategoryConfigByCategory(categoryIdString);


        assertEquals(categoryConfig.getId().toString(), response.id());

        Map<String, List<CompatibleOptionResponseDto>> options = response.options();
        assertEquals(1, options.size());

        List<CompatibleOptionResponseDto> optionList = options.get("DISK");
        assertEquals(1, optionList.size());

        CompatibleOptionResponseDto optionDto = optionList.get(0);

        verify(categoryConfigRepository, times(1)).findByCategoryId(categoryId);
        verify(compatibleOptionServiceImpl, times(1)).getByCategoryConfigId(categoryConfig.getId());
    }

    @Test
    void getCategoryConfigByCategory() {

    }

    @Test
    void getCategoryListResponses() {
        List<CategoryConfig> categoryConfigs = List.of(categoryConfig);

        when(categoryConfigRepository.findAll()).thenReturn(categoryConfigs);

        when(categoryConfigService.extractProductCount(any())).thenReturn(10L);


        List<CategoryListResponse> responses = categoryConfigService.getCategoryListResponses();


        assertEquals(categoryConfigs.size(), responses.size());

        verify(categoryConfigRepository, times(1)).findAll();
    }

    @Test
    void getCategoryAndCompatibleOption() {
        UUID categoryId = UUID.randomUUID();
        when(categoryConfigRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(categoryConfig));

        compatibleOption.setId(UUID.randomUUID());

        List<CompatibleOption> compatibleOptions = List.of(compatibleOption);
        when(compatibleOptionServiceImpl.getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId())).thenReturn(compatibleOptions);

        CompatibleOptionGetResponse response = categoryConfigService.getCategoryAndCompatibleOption(categoryId);

        assertEquals(categoryConfig.getCategory().getId().toString(), response.id());
        assertEquals(categoryConfig.getCategory().getCategoryName(), response.name());

        List<CompatibleOptionResponseDto> config = response.config();

        assertEquals(compatibleOptions.size(), config.size());

        verify(categoryConfigRepository, times(1)).findByCategoryId(categoryId);
        verify(compatibleOptionServiceImpl, times(1)).getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId());
    }
//
//    @Test
//    void testDeleteCategoryAndCategoryConfig() {
//        List<String> categoryIds = List.of(UUID.randomUUID().toString());
//        List<UUID> categoryUUIDs = categoryIds.stream().map(UUID::fromString).toList();
//
//        doNothing().when(categoryConfigRepository).deleteAllByCategoryId(categoryUUIDs);
//        doNothing().when(categoryService).deleteAllById(categoryUUIDs);
//
//        GenericResponse response = categoryConfigService.deleteCategoryAndCategoryConfig(categoryIds);
//
//        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
//        assertEquals("deleted category successfully", response.message());
//
//        verify(categoryConfigRepository, times(1)).deleteAllByCategoryId(categoryUUIDs);
//        verify(categoryService, times(1)).deleteAllById(categoryUUIDs);
//    }

    @Test
    void testUpdateCategoryAndConfigs() {

        when(categoryConfigRepository.findByCategoryId(any(UUID.class))).thenReturn(Optional.ofNullable(categoryConfig));
        when(categoryService.updateCategory(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name(), compatibleOptionEditResponse.thumbnail())).thenReturn(new CategoryResponse(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name(), compatibleOptionEditResponse.thumbnail()));
        doNothing().when(compatibleOptionServiceImpl).updateBulkCompatibleOptions(categoryConfig, compatibleOptionEditResponse.config());

        GenericResponse response = categoryConfigService.updateCategoryAndConfigs(compatibleOptionEditResponse);

        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
        assertEquals("updated category and config", response.message());

        verify(categoryService, times(1)).updateCategory(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name(), compatibleOptionEditResponse.thumbnail());
        verify(compatibleOptionServiceImpl, times(1)).updateBulkCompatibleOptions(categoryConfig, compatibleOptionEditResponse.config());
    }

    @Test
    void extractAttributesFromCompatibleOptions() {
        UUID categoryConfigId = UUID.randomUUID();
        List<CompatibleOption> mockCompatibleOptions = List.of(compatibleOption);
        when(compatibleOptionServiceImpl.getAllCompatibleOptionsByCategoryConfig(categoryConfigId)).thenReturn(mockCompatibleOptions);

        Map<String, List<CategoryConfigDisplay>> extractedAttributes = categoryConfigService.extractAttributesFromCompatibleOptions(categoryConfigId);
        assertNotNull(extractedAttributes);
        verify(compatibleOptionServiceImpl, times(1)).getAllCompatibleOptionsByCategoryConfig(categoryConfigId);

    }

    @Test
    void extractProductCount() {
        UUID categoryId = UUID.randomUUID();

        when(productRepository.countProductsByCategoryId(categoryId)).thenReturn(0L);

        Long result = categoryConfigService.extractProductCount(categoryId);

        verify(productRepository, times(1)).countProductsByCategoryId(categoryId);
        assertEquals(0L, result);
    }

    @Test
    void testUpdateExistingCategoryConfigs() {

        List<CategoryConfig> categoryConfigs = Collections.singletonList(categoryConfig);
        when(categoryConfigRepository.findAll()).thenReturn(categoryConfigs);

        doNothing().when(compatibleOptionService).updateBulkCompatibleOptions(any(CategoryConfig.class), anyList());
        categoryConfigService.updateExistingCategoryConfigs(List.of(attributeOption));
        verify(categoryConfigRepository, times(1)).findAll();
    }
}
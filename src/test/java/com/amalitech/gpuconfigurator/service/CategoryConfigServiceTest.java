package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
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

    private CompatibleOptionEditResponse compatibleOptionEditResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryConfigRequest = new CategoryConfigRequest("TestCategory",
                Collections.singletonList(new CompatibleOptionDTO("TestOption", "Type1", null, BigDecimal.TEN,
                        "Media1", "Unit1", true, true, 1.0, true, 1.0f, 2.0f, 0.1f)));

        category = Category.builder().id(UUID.randomUUID()).categoryName("TestCategory").build();
        categoryConfig = CategoryConfig.builder().id(UUID.randomUUID()).category(category).build();

        categoryConfig = CategoryConfig.builder()
                .id(UUID.randomUUID())
                .category(category)
                .compatibleOptions(new ArrayList<>())
                .build();

        compatibleOption = CompatibleOption.builder()
                .categoryConfig(categoryConfig)
                .name("SampleOption")
                .type("SampleType")
                .price(BigDecimal.valueOf(25.0))
                .media("/sample.png")
                .unit("SampleUnit")
                .isMeasured(true)
                .baseAmount(10.0F)
                .maxAmount(50.0F)
                .priceIncrement(5.0F)
                .priceFactor(1.2)
                .isCompatible(true)
                .isIncluded(true)
                .createdAt(LocalDateTime.now())
                .build();

        compatibleOptionEditResponse = CompatibleOptionEditResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("SampleCategory")
                .config(Arrays.asList(
                        CompatibleOptionResponseDto.builder()
                                .id("101")
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

    }


    @Test
    void testCreateCategoryConfig() {

        when(categoryService.createCategory(any())).thenReturn(category);
        when(categoryConfigRepository.save(any())).thenReturn(categoryConfig);

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
        when(categoryConfigRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(categoryConfig));

        when(compatibleOptionServiceImpl.getByCategoryConfigId(categoryConfig.getId())).thenReturn(List.of(compatibleOption));

        CategoryConfigResponseDto response = categoryConfigService.getCategoryConfigByCategory(categoryIdString);


        assertEquals(categoryConfig.getId().toString(), response.id());

        Map<String, List<CompatibleOptionResponseDto>> options = response.options();
        assertEquals(1, options.size());

        List<CompatibleOptionResponseDto> optionList = options.get("SampleType");
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

        when(categoryConfigService.extractAttributesFromCompatibleOptions(any())).thenReturn(Collections.emptyList());
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

        CompatibleOptionEditResponse response = categoryConfigService.getCategoryAndCompatibleOption(categoryId);

        assertEquals(categoryConfig.getCategory().getId().toString(), response.id());
        assertEquals(categoryConfig.getCategory().getCategoryName(), response.name());

        List<CompatibleOptionResponseDto> config = response.config();

        assertEquals(compatibleOptions.size(), config.size());

        verify(categoryConfigRepository, times(1)).findByCategoryId(categoryId);
        verify(compatibleOptionServiceImpl, times(1)).getAllCompatibleOptionsByCategoryConfig(categoryConfig.getId());
    }

    @Test
    void testDeleteCategoryAndCategoryConfig() {
        List<String> categoryIds = List.of(UUID.randomUUID().toString());
        List<UUID> categoryUUIDs = categoryIds.stream().map(UUID::fromString).toList();

        doNothing().when(categoryConfigRepository).deleteAllByCategoryId(categoryUUIDs);
        doNothing().when(categoryService).deleteAllById(categoryUUIDs);

        GenericResponse response = categoryConfigService.deleteCategoryAndCategoryConfig(categoryIds);

        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
        assertEquals("deleted category successfully", response.message());

        verify(categoryConfigRepository, times(1)).deleteAllByCategoryId(categoryUUIDs);
        verify(categoryService, times(1)).deleteAllById(categoryUUIDs);
    }

    @Test
    void testUpdateCategoryAndConfigs() {

        when(categoryService.updateCategory(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name())).thenReturn(new CategoryResponse(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name()));
        doNothing().when(compatibleOptionServiceImpl).updateBulkCompatibleOptions(compatibleOptionEditResponse.config());

        GenericResponse response = categoryConfigService.updateCategoryAndConfigs(compatibleOptionEditResponse);

        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
        assertEquals("updated category and config", response.message());

        verify(categoryService, times(1)).updateCategory(compatibleOptionEditResponse.id(), compatibleOptionEditResponse.name());
        verify(compatibleOptionServiceImpl, times(1)).updateBulkCompatibleOptions(compatibleOptionEditResponse.config());
    }

    @Test
    void extractAttributesFromCompatibleOptions() {
        UUID categoryConfigId = UUID.randomUUID();
        List<CompatibleOption> mockCompatibleOptions = List.of(compatibleOption);
        when(compatibleOptionServiceImpl.getAllCompatibleOptionsByCategoryConfig(categoryConfigId)).thenReturn(mockCompatibleOptions);

        List<String> extractedAttributes = categoryConfigService.extractAttributesFromCompatibleOptions(categoryConfigId);

        assertEquals(Arrays.asList("SampleType"), extractedAttributes);
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
}
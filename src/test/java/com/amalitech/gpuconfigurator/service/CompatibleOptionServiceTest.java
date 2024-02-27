package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleUpdateDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompatibleOptionServiceTest {

    @Mock
    private CompatibleOptionRepository compatibleOptionRepository;

    @InjectMocks
    private CompatibleOptionServiceImpl compatibleOptionService;

    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    private CompatibleOption compatibleOption;
    private CategoryConfig categoryConfig;
    private Category category;
    private CompatibleOptionResponseDto compatibleOptionResponseDto;
    private AttributeOption attributeOption;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(UUID.randomUUID())
                .categoryName("SampleCategory")
                .build();

        categoryConfig = CategoryConfig.builder()
                .id(UUID.randomUUID())
                .category(category)
                .compatibleOptions(new ArrayList<>())
                .build();

        attributeOption = AttributeOption.builder()
                .optionName("SampleOption")
                .priceAdjustment(BigDecimal.valueOf(25.0))
                .attribute(Attribute.builder()
                        .unit("SampleUnit")
                        .isMeasured(true)
                        .description("")
                        .attributeName("DISK")
                        .build())
                .id(UUID.randomUUID())
                .baseAmount(10.0F)
                .maxAmount(50.0F)
                .priceFactor(1.2)
                .build();

        compatibleOption = CompatibleOption.builder()
                .categoryConfig(categoryConfig)
                .isMeasured(true)
                .isCompatible(true)
                .size(2)
                .attributeOption(attributeOption)
                .isIncluded(true)
                .createdAt(LocalDateTime.now())
                .build();

         compatibleOptionResponseDto = CompatibleOptionResponseDto.builder()
                .compatibleOptionId("123")
                .name("Sample Option")
                .type("Type A")
                .price(new BigDecimal("50.00"))
                .media("Sample Media")
                .unit("Unit A")
                .isCompatible(true)
                .isIncluded(false)
                .isMeasured(true)
                .priceFactor(1.5)
                .priceIncrement(2.0f)
                .baseAmount(10.0f)
                .maxAmount(100.0f)
                .build();

    }

    @Test
    void addBulkCompatibleOptions_shouldAddBulkCompatibleOptions() {
        List<CompatibleOption> bulkList = List.of(compatibleOption);

        when(compatibleOptionRepository.saveAll(anyList())).thenReturn(bulkList);
        compatibleOptionService.addBulkCompatibleOptions(bulkList);

        verify(compatibleOptionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void updateBulkCompatibleOptions_shouldUpdateBulkCompatibleOptions() {
        CompatibleUpdateDto updateDto = CompatibleUpdateDto.builder()
                .compatibleOptionId(UUID.randomUUID().toString())
                .isCompatible(true)
                .isIncluded(false)
                .isMeasured(true)
                .attributeOptionId(attributeOption.getId().toString())
                .attributeId(attributeOption.getId().toString())
                .size(24)
                .build();

        CompatibleOption existingOption = CompatibleOption.builder()
                .id(UUID.fromString(updateDto.compatibleOptionId()))
                .attributeOption(attributeOption)
                .isCompatible(true)
                .isIncluded(true)
                .isMeasured(true)
                .build();


        when(compatibleOptionRepository.findByCategoryConfigIdAndAttributeOptionId(categoryConfig.getId(), UUID.fromString(updateDto.attributeOptionId()))).thenReturn(Optional.of(existingOption));
        when(attributeOptionRepository.findById(UUID.fromString(updateDto.attributeOptionId()))).thenReturn(Optional.of(attributeOption));


        compatibleOptionService.updateBulkCompatibleOptions(categoryConfig, Arrays.asList(updateDto));

        verify(compatibleOptionRepository, times(1)).save(existingOption);

        assertEquals(false, existingOption.getIsIncluded());
        assertEquals(true, existingOption.getAttributeOption().getAttribute().isMeasured());
    }

    @Test
    void getAllCompatibleOptionsByCategoryConfig() {
        UUID configId = UUID.randomUUID();
        List<CompatibleOption> expectedOptions = List.of(compatibleOption);

        when(compatibleOptionRepository.findAllByCategoryConfigId(configId)).thenReturn(expectedOptions);

        List<CompatibleOption> actualOptions = compatibleOptionService.getAllCompatibleOptionsByCategoryConfig(configId);

        assertEquals(expectedOptions, actualOptions);
        verify(compatibleOptionRepository, times(1)).findAllByCategoryConfigId(configId);
    }

    @Test
    void deleteAllCompatibleOptions_shouldDeleteAllOptions() {
        doNothing().when(compatibleOptionRepository).deleteAll();

        GenericResponse response = compatibleOptionService.deleteAllCompatibleOptions();


        assertEquals(201, response.status());
        assertEquals("deleted all compatible options", response.message());
        verify(compatibleOptionRepository, times(1)).deleteAll();
    }

    @Test
    void deleteCompatibleOption_shouldDeleteOptionById() {
        String optionId = UUID.randomUUID().toString();

        doNothing().when(compatibleOptionRepository).deleteById(UUID.fromString(optionId));

        GenericResponse response = compatibleOptionService.deleteCompatibleOption(optionId);

        assertEquals(201, response.status());
        assertEquals("deleted compatible option", response.message());
        verify(compatibleOptionRepository, times(1)).deleteById(UUID.fromString(optionId));
    }

    @Test
    void getByCategoryConfigId_shouldReturnListOfCompatibleOptions() {
        // Arrange
        UUID configId = UUID.randomUUID();

        when(compatibleOptionRepository.getByCategoryConfigId(configId)).thenReturn(List.of(compatibleOption));

        List<CompatibleOption> actualOptions = compatibleOptionService.getByCategoryConfigId(configId);


        assertEquals(List.of(compatibleOption), actualOptions);
        verify(compatibleOptionRepository, times(1)).getByCategoryConfigId(configId);
    }

}
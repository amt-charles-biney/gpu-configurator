package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
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
class CompatibleOptionServiceImplTest {

    @Mock
    private CompatibleOptionRepository compatibleOptionRepository;

    @InjectMocks
    private CompatibleOptionServiceImpl compatibleOptionService;

    private CompatibleOption compatibleOption;
    private CategoryConfig categoryConfig;
    private Category category;
    private CompatibleOptionResponseDto compatibleOptionResponseDto;

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

         compatibleOptionResponseDto = CompatibleOptionResponseDto.builder()
                .id("123")
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
        CompatibleOptionResponseDto updateDto = CompatibleOptionResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Updated Option")
                .type("Updated Type")
                .price(new BigDecimal("75.00"))
                .media("Updated Media")
                .unit("Updated Unit")
                .isCompatible(true)
                .isIncluded(false)
                .isMeasured(true)
                .priceFactor(2.0)
                .priceIncrement(3.0f)
                .baseAmount(15.0f)
                .maxAmount(150.0f)
                .build();

        CompatibleOption existingOption = CompatibleOption.builder()
                .id(UUID.fromString(updateDto.id()))
                .name("Original Option")
                .type("Original Type")
                .price(new BigDecimal("50.00"))
                .media("Original Media")
                .unit("Original Unit")
                .isCompatible(true)
                .isIncluded(true)
                .isMeasured(true)
                .priceFactor(1.5)
                .priceIncrement(2.0f)
                .baseAmount(10.0f)
                .maxAmount(100.0f)
                .build();


        when(compatibleOptionRepository.findById(UUID.fromString(updateDto.id()))).thenReturn(Optional.of(existingOption));

        compatibleOptionService.updateBulkCompatibleOptions(Arrays.asList(updateDto));

        verify(compatibleOptionRepository, times(1)).save(existingOption);

        assertEquals("Updated Option", existingOption.getName());
        assertEquals("Updated Type", existingOption.getType());
        assertEquals(new BigDecimal("75.00"), existingOption.getPrice());
        assertEquals("Updated Media", existingOption.getMedia());
        assertEquals("Updated Unit", existingOption.getUnit());
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
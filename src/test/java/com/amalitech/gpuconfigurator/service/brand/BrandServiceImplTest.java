package com.amalitech.gpuconfigurator.service.brand;

import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private AttributeServiceImpl attributeService;

    @Test
    void getAllBrands() {
        Attribute attr = Attribute.builder()
                .attributeName("Attribute")
                .description("Some description")
                .build();

        AttributeOption brand1 = AttributeOption.builder()
                .id(UUID.randomUUID())
                .brand("Brand 1")
                .media("Some media here")
                .attribute(attr)
                .build();
        AttributeOption brand2 = AttributeOption.builder()
                .id(UUID.randomUUID())
                .brand("Brand 2")
                .media("Some media here")
                .attribute(attr)
                .build();

        List<AttributeOption> expectedResponse = List.of(brand1, brand2);

        when(attributeService.getAllAttributeOptions()).thenReturn(expectedResponse);
        var response = brandService.getAllBrands();

        assertNotNull(response);

    }

    @Test
    void givenBrand_whenBrandIsEmpty_thenReturnEmpty() {
        //give - precondition or setup
        List<AttributeOption> expectedResponse = new ArrayList<>();

        // when - action or the behaviour under test
        when(attributeService.getAllAttributeOptions()).thenReturn(expectedResponse);

        //then - verify the output
        var response = brandService.getAllBrands();
        assertEquals(0, response.size());
    }
}
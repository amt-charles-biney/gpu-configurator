package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AttributeControllerTest {

    @MockBean
    private AttributeServiceImpl attributeService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private AttributeResponse attributeResponse;
    private CreateAttributesRequest createAttributesRequest;
    private AttributeOptionResponseDto attributeOptionResponseDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        createAttributesRequest = CreateAttributesRequest.builder()
                .attributeName("SampleAttribute")
                .isMeasured(true)
                .description("Sample Description")
                .unit("SampleUnit")
                .variantOptions(List.of(
                        CreateAttributeOptionRequest.builder().name("Option1").price(BigDecimal.valueOf(10)).build(),
                        CreateAttributeOptionRequest.builder().name("Option2").price(BigDecimal.valueOf(20)).build()
                ))
                .build();

         attributeOptionResponseDto = AttributeOptionResponseDto.builder()
                .id("OptionId1")
                .optionName("Option1")
                .additionalInfo(AttributeVariantDto.builder()
                        .baseAmount(50.0f)
                        .maxAmount(100.0f)
                        .priceFactor(1.2)
                        .build())
                .optionPrice(BigDecimal.valueOf(10.0))
                .optionMedia("Media1")
                .attribute(AttributeResponseDto.builder()
                        .name("SampleAttribute")
                        .id(UUID.randomUUID().toString())
                        .isMeasured(true)
                        .build())
                .build();

         attributeResponse = AttributeResponse.builder()
                .id(UUID.randomUUID())
                .attributeName("SampleAttribute")
                .isMeasured(true)
                .unit("SampleUnit")
                .description("Sample Description")
                .attributeOptions(List.of(attributeOptionResponseDto))
                .build();
    }


    @Test
    public void testCreateAllAttributeandAttributeOptionsBulk() throws Exception {
        // Mock request dat

        List<AttributeResponse> attributeResponses = List.of(attributeResponse);
        when(attributeService.createAttributeAndAttributeOptions(any())).thenReturn(attributeResponses);

        // Perform the HTTP request and verify the response
        mockMvc.perform(post("/v1/admin/attributes/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAttributesRequest)))
                .andExpect(status().isCreated());

        verify(attributeService, times(1)).createAttributeAndAttributeOptions(eq(createAttributesRequest));
    }
}

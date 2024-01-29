package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionEditResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionGetResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AttributeControllerTest {

    @MockBean
    private AttributeServiceImpl attributeService;
    @Autowired private MockMvc mockMvc;
    private static ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private AttributeController attributeController;
    private AttributeResponse attributeResponse;
    private AttributeOptionResponseDto attributeOptionResponseDto;
    private CreateAttributesRequest createAttributesRequest;
    private CompatibleOptionGetResponse compatibleOptionGetResponse;
    private CompatibleOptionResponseDto compatibleOptionResponseDto;

    private UUID attributeId;

    @BeforeEach
    public void setUp() {

        attributeId = UUID.randomUUID();

        createAttributesRequest = CreateAttributesRequest.builder()
                .attributeName("SampleAttribute")
                .isMeasured(true)
                .description("Sample Description")
                .unit("SampleUnit")
                .variantOptions(List.of(
                        CreateAttributeOptionRequest.builder().name("Option1").price(BigDecimal.valueOf(10)).maxAmount(200.0F).baseAmount(23.1F).priceFactor(1.4).media("/hello.png").build(),
                        CreateAttributeOptionRequest.builder().name("Option2").price(BigDecimal.valueOf(20)).baseAmount(10F).maxAmount(12F).media("/help.png").priceFactor(23.6).build()
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
                .attributeId(attributeId.toString())
                .attributeOptionId(UUID.randomUUID().toString())
                .size(20)
                .build();

        compatibleOptionGetResponse = CompatibleOptionGetResponse
                .builder()
                .name("")
                .id(null)
                .config(List.of(compatibleOptionResponseDto))
                .build();
    }

    @Test
    public void testCreateAllAttributeAndAttributeOptionsBulk() throws Exception {

        List<AttributeResponse> attributeResponses = List.of(attributeResponse);
        when(attributeService.createAttributeAndAttributeOptions(any(CreateAttributesRequest.class))).thenReturn(attributeResponses);


        mockMvc.perform(post("/api/v1/admin/attributes/bulk")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAttributesRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAllAttributes_shouldReturnAllAttributes() throws Exception {

        when(attributeService.getAllAttributes()).thenReturn(List.of(attributeResponse));

        mockMvc.perform(get("/api/v1/admin/attributes"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteAllAttributes_shouldDeleteAttributesWithPassedIds() throws Exception {
        List<String> attributes= List.of(UUID.randomUUID().toString());
        when(attributeService.deleteBulkAttributes(attributes)).thenReturn(any(GenericResponse.class));

        mockMvc.perform(delete("/api/v1/admin/attributes/all")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(attributes)))
                .andExpect(status().isAccepted());

    }

    @Test
    void testUpdateAllAttributeAndAttributeOptions_shouldReturnUpdatedAttributes() throws Exception {

        UpdateAttributeDto updateAttributeDto = new UpdateAttributeDto(
                UUID.randomUUID().toString(),
                "UpdatedAttributeName",
                true,
                "UpdatedAttributeDescription",
                "UpdatedUnit",
                List.of(
                        new UpdateAttributeOptionDto(
                                "existingOptionId",
                                "UpdatedOptionName",
                                BigDecimal.valueOf(30.0),
                                "/updated_media.png",
                                25.0F,
                                150.0F,
                                2.0
                        )
                )
        );


        AttributeResponse updatedAttributeResponse = AttributeResponse.builder()
                .id(UUID.randomUUID())
                .attributeName("UpdatedSampleAttribute")
                .isMeasured(true)
                .unit("UpdatedSampleUnit")
                .description("UpdatedSample Description")
                .build();

        List<AttributeResponse> updatedAttributeResponses = List.of(updatedAttributeResponse);

        when(attributeService.bulkUpdateAttributeAndAttributeOptions(any(UpdateAttributeDto.class)))
                .thenReturn(updatedAttributeResponses);

        mockMvc.perform(put("/api/v1/admin/attributes/bulk")
                        .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateAttributeDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAttribute_shouldReturnAttribute() throws Exception {
        String attributeId = UUID.randomUUID().toString();

        when(attributeService.getAttributeById(UUID.fromString(attributeId))).thenReturn(attributeResponse);

        mockMvc.perform(get("/api/v1/admin/attributes/{attributeId}", attributeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteAttribute_shouldReturnSuccess() throws Exception {

        String attributeId = UUID.randomUUID().toString();

        when(attributeService.deleteAttributeById(UUID.fromString(attributeId))).thenReturn(any(GenericResponse.class));

        mockMvc.perform(delete("/api/v1/admin/attributes/{attributeId}", attributeId))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAttributesInCompatibleForm() throws Exception {

        when(attributeService.getAllAttributeOptionsEditable()).thenReturn(compatibleOptionGetResponse);

        mockMvc.perform(get("/api/v1/admin/attributes/config"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").value(""));
    }
}

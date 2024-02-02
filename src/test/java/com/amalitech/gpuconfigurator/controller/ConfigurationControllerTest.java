package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ConfigurationControllerTest {

    @InjectMocks
    private ConfigurationController configurationController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigurationServiceImpl configurationService;

    @Mock
    private ConfigurationRepository configurationRepository;

    @Test
    void configuration() throws Exception {
        String productId = "56c35cf3-a72f-41d2-bd4f-0727133e0efe";
        Boolean warranty = true;
        String components = "b65d7dc4-11e9-4f7e-b11a-ea81d885a73d,fde4787a-029b-4115-8237-f74b314119dc";

        ConfigurationResponseDto expectedResult = ConfigurationResponseDto.builder()
                .Id(null)
                .totalPrice(new BigDecimal("679.26"))
                .productId(productId)
                .productPrice(new BigDecimal("100.0"))
                .configuredPrice(new BigDecimal("443.41"))
                .configured(Arrays.asList(
                        ConfigOptions.builder()
                                .id(null)
                                .optionId("b65d7dc4-11e9-4f7e-b11a-ea81d885a73d")
                                .optionName("Ultra HD Webcam")
                                .optionType("Camera")
                                .optionPrice(new BigDecimal("119.985"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("64.00"))
                                .size("256")
                                .build(),
                        ConfigOptions.builder()
                                .id(null)
                                .optionId("fde4787a-029b-4115-8237-f74b314119dc")
                                .optionName("High-Fidelity Surround Sound Speakers")
                                .optionType("Audio")
                                .optionPrice(new BigDecimal("323.421328125"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("512.00"))
                                .size("1064")
                                .build()
                ))
                .vat(new BigDecimal("135.85"))
                .warranty(null)
                .build();

        when(configurationService.configuration(productId, warranty, components))
                .thenReturn(expectedResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/config/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .param("warranty", String.valueOf(warranty))
                        .param("components", components)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("679.26"))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.configuredPrice").value("443.41"))
                .andExpect(jsonPath("$.configured[0].optionId").value("b65d7dc4-11e9-4f7e-b11a-ea81d885a73d"))
                .andExpect(jsonPath("$.configured[1].optionId").value("fde4787a-029b-4115-8237-f74b314119dc"))
                .andExpect(jsonPath("$.vat").value("135.85"));

        verify(configurationService, times(1)).configuration(productId, warranty, components);

    }


    @Test
    void createConfiguration() throws Exception {
        String productId = "56c35cf3-a72f-41d2-bd4f-0727133e0efe";
        Boolean warranty = true;
        Boolean save = false;
        String components = "b65d7dc4-11e9-4f7e-b11a-ea81d885a73d,fde4787a-029b-4115-8237-f74b314119dc";

        ConfigurationResponseDto expectedResult = ConfigurationResponseDto.builder()
                .Id(UUID.randomUUID().toString())
                .totalPrice(new BigDecimal("679.26"))
                .productId(productId)
                .productPrice(new BigDecimal("100.0"))
                .configuredPrice(new BigDecimal("443.41"))
                .configured(Arrays.asList(
                        ConfigOptions.builder()
                                .id(UUID.fromString(UUID.randomUUID().toString()))
                                .optionId("b65d7dc4-11e9-4f7e-b11a-ea81d885a73d")
                                .optionName("Ultra HD Webcam")
                                .optionType("Camera")
                                .optionPrice(new BigDecimal("119.985"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("64.00"))
                                .size("256")
                                .build(),
                        ConfigOptions.builder()
                                .id(UUID.fromString(UUID.randomUUID().toString()))
                                .optionId("fde4787a-029b-4115-8237-f74b314119dc")
                                .optionName("High-Fidelity Surround Sound Speakers")
                                .optionType("Audio")
                                .optionPrice(new BigDecimal("323.421328125"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("512.00"))
                                .size("1064")
                                .build()
                ))
                .vat(new BigDecimal("135.85"))
                .warranty(null)
                .build();

        when(configurationService.saveConfiguration(productId, warranty, save, components))
                .thenReturn(expectedResult);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/config/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .param("warranty", String.valueOf(warranty))
                        .param("save", String.valueOf(save))
                        .param("components", components)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("679.26"))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.configuredPrice").value("443.41"))
                .andExpect(jsonPath("$.configured[0].optionId").value("b65d7dc4-11e9-4f7e-b11a-ea81d885a73d"))
                .andExpect(jsonPath("$.configured[1].optionId").value("fde4787a-029b-4115-8237-f74b314119dc"))
                .andExpect(jsonPath("$.vat").value("135.85"));

        verify(configurationService, times(1)).saveConfiguration(productId, warranty, save, components);
    }

    @Test
    void getOneConfiguration() throws Exception {
        String configId = "f0d8b146-6067-46dc-b9f4-3257b60ee353";

        Configuration expectedConfiguration = Configuration.builder()
                .id(UUID.fromString(configId))
                .totalPrice(new BigDecimal("679.26"))
                .product(Product.builder()
                        .id(UUID.fromString("56c35cf3-a72f-41d2-bd4f-0727133e0efe"))
                        .productName("Test Product")
                        .productDescription("Sample product description")
                        .productPrice(100.0)
                        .build())
                .configuredOptions(Arrays.asList(
                        ConfigOptions.builder()
                                .id(UUID.fromString("48c52c3d-bb43-40f3-82fd-e27d5ab1ee52"))
                                .optionId("b65d7dc4-11e9-4f7e-b11a-ea81d885a73d")
                                .optionName("Ultra HD Webcam")
                                .optionType("Camera")
                                .optionPrice(new BigDecimal("119.985"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("64.00"))
                                .size("256")
                                .build(),
                        ConfigOptions.builder()
                                .id(UUID.fromString("8a407dfe-39fc-4b4e-965e-688d10073cfa"))
                                .optionId("fde4787a-029b-4115-8237-f74b314119dc")
                                .optionName("High-Fidelity Surround Sound Speakers")
                                .optionType("Audio")
                                .optionPrice(new BigDecimal("323.421328125"))
                                .isMeasured(true)
                                .baseAmount(new BigDecimal("512.00"))
                                .size("1064")
                                .build()
                ))
                .build();

        when(configurationService.getSpecificConfiguration(configId))
                .thenReturn(expectedConfiguration);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/config/one/{configId}", configId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(configId))
                .andExpect(jsonPath("$.totalPrice").value("679.26"));

        verify(configurationService, times(1)).getSpecificConfiguration(configId);
    }


}
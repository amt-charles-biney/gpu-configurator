package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static ObjectMapper objectMapper;
    @MockBean
    private CategoryConfigServiceImpl categoryConfigService;
    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void addConfig() throws Exception {
        CategoryConfigRequest request = new CategoryConfigRequest("CategoryName", Collections.emptyList());
        GenericResponse response = new GenericResponse(200, "Config added successfully");

        Mockito.when(categoryConfigService.createCategoryConfig(Mockito.any())).thenReturn(response);

        ResultActions result = mockMvc.perform(post("/api/v1/admin/category/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(response.status()))
                .andExpect(jsonPath("$.message").value(response.message()));
    }

    @Test
    void getConfigs() throws Exception {
        String categoryId = UUID.randomUUID().toString();
        CategoryConfigResponseDto responseDto = new CategoryConfigResponseDto(UUID.randomUUID().toString(), new CategoryResponse( categoryId, "CategoryName"), Collections.emptyMap());

        Mockito.when(categoryConfigService.getCategoryConfigByCategory(categoryId)).thenReturn(responseDto);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/category/{categoryId}/config", categoryId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.category.id").value(responseDto.category().id()))
                .andExpect(jsonPath("$.category.name").value(responseDto.category().name()));
    }

    @Test
    void getConfigsUser() throws Exception {
        String categoryId = UUID.randomUUID().toString();
        CategoryConfigResponseDto responseDto = new CategoryConfigResponseDto(UUID.randomUUID().toString(), new CategoryResponse(categoryId, "CategoryName"), Collections.emptyMap());

        Mockito.when(categoryConfigService.getCategoryConfigByCategory(categoryId)).thenReturn(responseDto);

        ResultActions result = mockMvc.perform(get("/api/v1/category/{categoryId}/config", categoryId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.category.id").value(responseDto.category().id()))
                .andExpect(jsonPath("$.category.name").value(responseDto.category().name()));
    }

    @Test
    void getAllCategoryConfig() throws Exception {

        List<CategoryListResponse> responseList = Collections.singletonList(new CategoryListResponse("CategoryName", UUID.randomUUID().toString(), Collections.emptyList(), 0L));

        Mockito.when(categoryConfigService.getCategoryListResponses()).thenReturn(responseList);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/category/config"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseList.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(responseList.get(0).name()));
    }

    @Test
    void testGetAllCategoryConfigById() throws Exception {
        String categoryId = UUID.randomUUID().toString();
        CompatibleOptionEditResponse response = new CompatibleOptionEditResponse("CategoryName", categoryId, Collections.emptyList());

        Mockito.when(categoryConfigService.getCategoryAndCompatibleOption(UUID.fromString(categoryId))).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/category/config/{categoryId}", categoryId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }

    @Test
    void testUpdate() throws Exception {
        CompatibleOptionEditResponse request = new CompatibleOptionEditResponse("CategoryName", "1", Collections.emptyList());
        GenericResponse response = new GenericResponse(200, "Category and config updated successfully");

        Mockito.when(categoryConfigService.updateCategoryAndConfigs(Mockito.any())).thenReturn(response);

        ResultActions result = mockMvc.perform(put("/api/v1/admin/category/config/{categoryId}", request.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(response.status()))
                .andExpect(jsonPath("$.message").value(response.message()));
    }
    @Test
    void testDeleteAllCategory() throws Exception {
        List<String> categoryIds = Collections.singletonList(UUID.randomUUID().toString());
        GenericResponse response = new GenericResponse(200, "Deleted category successfully");

        Mockito.when(categoryConfigService.deleteCategoryAndCategoryConfig(categoryIds)).thenReturn(response);

        ResultActions result = mockMvc.perform(delete("/api/v1/admin/category/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryIds)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(response.status()))
                .andExpect(jsonPath("$.message").value(response.message()));
    }}
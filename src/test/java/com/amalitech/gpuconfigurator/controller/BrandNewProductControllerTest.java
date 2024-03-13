package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.product.ProductService;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class BrandNewProductControllerTest {

    @InjectMocks
    private BrandNewProductController brandNewProductController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;


    @Test
    @DisplayName("Test success for getting newly created product")
    void getBrandNewProducts() throws Exception {
        Category category = Category.builder().categoryName("Test").build();

        List<FeaturedProductDto> expectedProduct = Arrays.asList(
                FeaturedProductDto.builder()
                        .id("c6409193-44e8-4791-1232-c58f2e2aba4b")
                        .productName("Product 1")
                        .productPrice(BigDecimal.valueOf(100))
                        .productBrand(category.getCategoryName())
                        .build(),
                FeaturedProductDto.builder()
                        .id("c6409193-44e8-4791-1232-c58f2e2aba4b")
                        .productName("Product 2")
                        .productPrice(BigDecimal.valueOf(100))
                        .productBrand(category.getCategoryName())
                        .build()

        );

        when(productService.getNewProducts()).thenReturn(expectedProduct);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/new"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
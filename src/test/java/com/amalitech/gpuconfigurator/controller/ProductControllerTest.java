package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    @DisplayName("Test Success for creating new product")
    void addProduct() {
        ProductDto requestDto = ProductDto
                .builder()
                .productId("c58f2e2aba4b")
                .productName("New Product")
                .productDescription("Serverless server")
                .productPrice(100.00)
                .productId("1234")
                .category("SERVER")
                .build();

        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("file1", "originalFilename1.txt", "text/plain", "File content 1".getBytes()),
                new MockMultipartFile("file2", "originalFilename2.txt", "text/plain", "File content 2".getBytes())
        );

        CreateProductResponseDto expectedResponse = CreateProductResponseDto
                .builder()
                .productName(requestDto.getProductName())
                .productName(requestDto.getProductId())
                .productAvailability()
                .build();

        when(productService.createProduct(requestDto, files)).thenReturn(expectedResponse);
        CreateProductResponseDto response = productController.addProduct(requestDto, files);

        assertNotNull(response);
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getProductByProductId() {
    }

    @Test
    void deleteProduct() {
    }
}
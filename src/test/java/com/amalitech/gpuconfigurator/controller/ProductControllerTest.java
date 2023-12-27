package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

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

        List<String> imageUrls = Arrays.asList(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg"
        );

        CreateProductResponseDto expectedResponse = CreateProductResponseDto
                .builder()
                .productName(requestDto.getProductName())
                .productId(requestDto.getProductId())
                .productDescription(requestDto.getProductDescription())
                .productPrice(requestDto.getProductPrice())
                .productId(requestDto.getProductId())
                .productCategory(requestDto.getCategory())
                .productAvailability(true)
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrls)
                .build();

        when(productService.createProduct(requestDto, files)).thenReturn(expectedResponse);
        CreateProductResponseDto response = productController.addProduct(requestDto, files);

        assertNotNull(response);
        assertEquals(response.getProductId(), expectedResponse.getProductId());
        assertEquals(response.getImageUrl().size(), expectedResponse.getImageUrl().size());
        assertEquals(response.getProductPrice(), expectedResponse.getProductPrice());
    }

    @Test
    @DisplayName("Test success for getting all product")
    void getAllProducts() {

        Category category = Category.builder().categoryName("GPU").build();

        List<Product> expectedProduct = Arrays.asList(
                Product.builder()
                        .id(UUID.fromString("c6409193-44e8-4791-b811-c58f2e2aba4b"))
                        .productName("Product 1")
                        .productDescription("powerful gpu")
                        .productPrice(10.00)
                        .productAvailability(true)
                        .category(category)
                        .createdAt(LocalDateTime.now())
                        .productId("1234")
                        .build(),
                Product.builder()
                        .id(UUID.fromString("c6409193-44e8-4791-1232-c58f2e2aba4b"))
                        .productName("Product 2")
                        .productDescription("powerful gpu")
                        .productPrice(20.00)
                        .productAvailability(true)
                        .category(category)
                        .createdAt(LocalDateTime.now())
                        .productId("abcd")
                        .build()
        );

        when(productService.getAllProduct()).thenReturn(expectedProduct);
        var response = productController.getAllProducts();
        assertNotNull(response);
        assertEquals(response.size(), expectedProduct.size());
        assertEquals(response.get(0).getId(), expectedProduct.get(0).getId());
    }

    @Test
    void getProductByProductId() {

        Category category = Category.builder().categoryName("GPU").build();

        Product product = Product.builder()
                .id(UUID.fromString("c6409193-44e8-4791-b811-c58f2e2aba4b"))
                .productName("Product 1")
                .productDescription("powerful gpu")
                .productPrice(10.00)
                .productAvailability(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .productId("1234")
                .build();

        when(productService.getProductByProductId(product.getProductId())).thenReturn(product);
        var response = productService.getProductByProductId("1234");
        assertNotNull(response);
        assertEquals(response.getProductId(), product.getProductId());
    }

    @Test
    void getProductByProductIdFailure() {

        when(productRepository.getProductByProductId(Mockito.anyString())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productController.getProductByProductId("1234");
        });

        assertEquals("1234 Notfound", exception.getMessage());

    }


}
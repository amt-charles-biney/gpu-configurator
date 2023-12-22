package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.model.Otp;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductService {
    CreateProductResponseDto createProduct(ProductDto request, MultipartFile file);

    void deleteProductById(UUID id) throws NoSuchFieldException;

    Otp.Product getProductByProductId(String productId);
}

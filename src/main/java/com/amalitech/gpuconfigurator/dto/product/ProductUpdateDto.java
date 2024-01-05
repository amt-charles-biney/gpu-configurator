package com.amalitech.gpuconfigurator.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductUpdateDto {
    String productName;
    String productDescription;
    Double productPrice;
    String productId;
    Boolean availability;
    Integer inStock;
    String category;
    List<MultipartFile> files;
    MultipartFile coverImage;
}
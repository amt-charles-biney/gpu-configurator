package com.amalitech.gpuconfigurator.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDto {
    String productName;
    String productDescription;
    Double productPrice;
    String productId;
    Boolean availability;
    Integer inStock;
    String category;
    String brand;
}
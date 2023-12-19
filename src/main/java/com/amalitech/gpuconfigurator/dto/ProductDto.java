package com.amalitech.gpuconfigurator.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto{

    @NotNull(message = "Product Name cannot be null")
    @NotEmpty(message = "Product Description cannot be empty")
    private String productName;

    @NotNull(message = "Product Description cannot be null")
    @NotEmpty(message = "Product Description cannot be empty")
   private String productDescription;

    @NotNull(message = "Product Price cannot be null")
    @PositiveOrZero(message = "Product price cannot be negative")
    private Double productPrice;

    private String category;
}
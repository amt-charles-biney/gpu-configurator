package com.amalitech.gpuconfigurator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateProductResponseDto{
   private String productName;
    private String productDescription;
    private Double productPrice;
    private Boolean productAvailability;
    private LocalDateTime createdAt;
}

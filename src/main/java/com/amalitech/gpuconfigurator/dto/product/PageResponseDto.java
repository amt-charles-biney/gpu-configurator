package com.amalitech.gpuconfigurator.dto.product;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Data
@RequiredArgsConstructor
public class PageResponseDto {
    private List<ProductResponse> products;
    private long total;
}

package com.amalitech.gpuconfigurator.dto.search;

import com.amalitech.gpuconfigurator.model.ProductDocument;
import lombok.Data;

import java.util.List;

@Data
public class ProductSearchResponseDto {
    private List<ProductDocument> products;
    private Long total;
}
package com.amalitech.gpuconfigurator.service.product;


import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public List<FeaturedProductDto> getNewProducts() {
        LocalDateTime timeRequest = LocalDateTime.now().minusHours(24);
        var products = productRepository.getBrandNewProducts(timeRequest).orElse(Collections.emptyList());
        return products.stream().map(product -> FeaturedProductDto.builder()
                .productName(product.getProductName())
                .coverImage(product.getProductCase().getCoverImageUrl())
                .build()).toList();
    }

}

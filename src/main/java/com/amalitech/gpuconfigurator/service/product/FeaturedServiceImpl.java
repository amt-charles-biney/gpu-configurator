package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeaturedServiceImpl implements FeaturedService {
    private final ProductRepository productRepository;
    @Override
    public List<Product> getAllFeaturedProduct() {
        return productRepository.getFeaturedProduct().orElse(Collections.emptyList());
    }
}

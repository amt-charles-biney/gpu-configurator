package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    ProductBrand findByProductId(UUID productId);
}

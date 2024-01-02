package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    ProductBrand findByProductId(UUID productId);
}

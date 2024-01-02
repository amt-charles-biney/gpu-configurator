package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    Optional<ProductBrand> findByProductId(UUID productId);
}

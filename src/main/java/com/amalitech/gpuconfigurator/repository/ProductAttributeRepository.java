package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID> {

}

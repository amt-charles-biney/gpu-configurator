package com.amalitech.gpuconfigurator.repository.category;

import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryConfigRepository extends JpaRepository<CategoryConfig, UUID> {
}

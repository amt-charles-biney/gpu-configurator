package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.CategoryConfig;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryConfigRepository extends JpaRepository<CategoryConfig, UUID> {
   Optional<CategoryConfig> findByCategoryId(UUID uuid);

    @Modifying
    @Query("DELETE FROM CategoryConfig cc WHERE cc.category.id IN :categoryIds")
    void deleteAllByCategoryId(@Param("categoryIds") List<UUID> categoryIds);
}

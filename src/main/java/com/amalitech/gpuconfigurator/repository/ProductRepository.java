package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Modifying
    void deleteById(UUID id);

    @Query("SELECT i FROM Product i WHERE i.productId =?1")
    Optional<Product> getProductByProductId(String productId);

    @Query("SELECT p FROM Product p WHERE p.featured = true")
    Optional<List<FeaturedProductAbstraction>> getFeaturedProduct();

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :startDate")
    Optional<List<FeaturedProductAbstraction>> getBrandNewProducts(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countProductsByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findProductsByCategoryIds(@Param("categoryIds") List<UUID> categoryIds);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    List<Product> findProductsByCategoryName(UUID categoryId);

    @Query("SELECT p FROM Product p WHERE p.productCase.id = ?1")
    List<Product> findProductsByProductCase(UUID caseId);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId")
    List<FeaturedProductAbstraction> selectAllProductByCategory(UUID categoryId);

    List<Product> findAllByCategoryId(UUID id);

    @Query("SELECT COUNT(o) FROM Product o")
    Long productsTotal();

    Page<Product> findAllByProductNameContainingIgnoreCase(String query, Pageable pageable);
}
package com.amalitech.gpuconfigurator.repository.product;

import com.amalitech.gpuconfigurator.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {


    @Modifying
    void deleteById(UUID id);

    @Query("SELECT i FROM Product i WHERE i.productId =?1")
    Optional<Product> getProductByProductId(String productId);
}

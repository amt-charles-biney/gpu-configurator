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

//    @Query("SELECT s FROM Product s WHERE s.id = ?1")
//    Optional<Product> findById(UUID id);

    @Query("SELECT p FROM Product p WHERE p.productName = ?1")
    List<Product> findProductsByName(String productName);


    @Modifying
    void deleteById(UUID id);
}

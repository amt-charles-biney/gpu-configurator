package com.amalitech.gpuconfigurator.model.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_availability", nullable = false)
    private Boolean productAvailability;

    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        productAvailability = true;
    }


    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;

}

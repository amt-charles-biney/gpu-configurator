package com.amalitech.gpuconfigurator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product_brand")
public class ProductBrand {

    @Id
    @Column(nullable = false, unique = true)
    private UUID productId;

    @Column(nullable = false)
    private UUID brandId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deleted_at;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

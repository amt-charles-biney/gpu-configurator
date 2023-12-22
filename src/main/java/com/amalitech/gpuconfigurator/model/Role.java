package com.amalitech.gpuconfigurator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum Role {
    USER,
    ADMIN;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity(name = "Category")
    @Table(name = "category")
    public static class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "category_name", nullable = false)
        private String categoryName;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @OneToMany(
                mappedBy = "category",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private List<Otp.Product> products = new ArrayList<>();

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
        }


        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;
    }
}

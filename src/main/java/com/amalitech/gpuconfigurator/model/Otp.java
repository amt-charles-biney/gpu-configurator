package com.amalitech.gpuconfigurator.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private OtpType type;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity(name = "Product")
    @Table(name = "product")
    public static class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "product_name", nullable = false)
        private String productName;

        @Column(name = "product_id", nullable = false)
        private String productId;

        @Column(name = "product_description", nullable = false)
        private String productDescription;

        @Column(name = "product_price", nullable = false)
        private Double productPrice;

        @JsonIgnoreProperties("products")
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(
                name = "category_id_fk"
        ))
        private Role.Category category;

        @Column(name = "image_url", nullable = false)
        private List<String> imageUrl;

        @Column(name = "product_availability", nullable = false)
        private Boolean productAvailability;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            productAvailability = true;
        }


        private LocalDateTime updated_at;

        private LocalDateTime deleted_at;

    }
}

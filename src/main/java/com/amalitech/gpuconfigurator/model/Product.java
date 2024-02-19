package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "service_charge", nullable = false)
    private Double serviceCharge;

    @Column(name = "product_price", nullable = false)
    private BigDecimal totalProductPrice;

    @Column(name = "base_config_price", nullable = false)
    private BigDecimal baseConfigPrice;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "case_id", referencedColumnName = "id", foreignKey = @ForeignKey(
            name = "case_id_fk"
    ))
    private Case productCase;

    @Column(name = "product_instock", nullable = false)
    private Integer inStock;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(
            name = "category_id_fk"
    ))
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Configuration> configurations;

    @Column(name = "product_featured")
    private Boolean featured;

    @Column(name = "product_availability", nullable = false)
    private Boolean productAvailability;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        productAvailability = true;
        featured = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getProductName(), product.getProductName())
                && Objects.equals(getProductId(), product.getProductId()) && Objects.equals(getProductDescription(), product.getProductDescription())
                && Objects.equals(getServiceCharge(), product.getServiceCharge()) && Objects.equals(getTotalProductPrice(), product.getTotalProductPrice())
                && Objects.equals(getBaseConfigPrice(), product.getBaseConfigPrice()) && Objects.equals(getProductCase(), product.getProductCase())
                && Objects.equals(getInStock(), product.getInStock()) && Objects.equals(getCategory(), product.getCategory())
                && Objects.equals(getConfigurations(), product.getConfigurations()) && Objects.equals(getFeatured(), product.getFeatured())
                && Objects.equals(getProductAvailability(), product.getProductAvailability()) && Objects.equals(getCreatedAt(), product.getCreatedAt())
                && Objects.equals(getUpdatedAt(), product.getUpdatedAt()) && Objects.equals(getDeletedAt(), product.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProductName(), getProductId(), getProductDescription(),
                getServiceCharge(), getTotalProductPrice(), getBaseConfigPrice(), getProductCase(),
                getInStock(), getCategory(), getConfigurations(), getFeatured(), getProductAvailability(),
                getCreatedAt(), getUpdatedAt(), getDeletedAt());
    }
}
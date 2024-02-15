package com.amalitech.gpuconfigurator.model.attributes;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attribute_options")
public class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "price_adjustment")
    private BigDecimal priceAdjustment;

    @Column(name = "media")
    private String media;

    @Column(name = "brand")
    private String brand;

    @Column(name="base_amount")
    private Float baseAmount;

    @Column(name="max_amount")
    private Float maxAmount;

    @Column(name="price_increment")
    private Double priceFactor;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Attribute attribute;

    private Integer inStock;

    @Column(name = "incompatible_attribute_option")
    private List<UUID> incompatibleAttributeOptions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}

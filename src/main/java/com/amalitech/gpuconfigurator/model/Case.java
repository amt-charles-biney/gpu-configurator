package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "cover_image_url", nullable = false)
    private String coverImageUrl;

    @Column(name = "image_urls", nullable = false)
    private List<String> imageUrls;

    @Column
    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "case_incompatible_variants",
            joinColumns = @JoinColumn(name = "case_id"),
            inverseJoinColumns = @JoinColumn(name = "incompatible_variant_id")
    )
    private List<AttributeOption> incompatibleVariants;

    @ManyToMany(mappedBy = "categoryCase", cascade = CascadeType.PERSIST)
    private List<Category> categories;

    @OneToMany(mappedBy = "productCase", cascade = {CascadeType.REMOVE})
    private List<Product> products;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PreRemove
    public void preRemove() {
        for (Category category : categories) {
            category.getCategoryCase().remove(this);
        }
        categories.clear();
    }
}
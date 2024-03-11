package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compatibleOption")
public class CompatibleOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private CategoryConfig categoryConfig;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AttributeOption attributeOption;

    private Boolean isCompatible;

    private Boolean isMeasured;

    private Boolean isIncluded;

    private Integer size;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompatibleOption that = (CompatibleOption) o;
        return Objects.equals(id, that.id) && Objects.equals(categoryConfig, that.categoryConfig) && Objects.equals(attributeOption, that.attributeOption)
                && Objects.equals(isCompatible, that.isCompatible) && Objects.equals(isMeasured, that.isMeasured)
                && Objects.equals(isIncluded, that.isIncluded) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryConfig, attributeOption, isCompatible, isMeasured, isIncluded, size);
    }
}

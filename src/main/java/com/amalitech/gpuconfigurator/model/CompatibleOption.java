package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compatibleOption" ,uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "attribute_id"})})
public class CompatibleOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne
    @JoinColumn(
            name = "category_config_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "category_config_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
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
}

package com.amalitech.gpuconfigurator.model.category;

import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
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
@Entity
@Table(name = "compatibleOption")
public class CompatibleOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private CategoryConfig categoryConfig;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.EAGER)
    private AttributeOption attributeOption;

    private Boolean isCompatible;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}

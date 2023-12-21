package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import com.amalitech.gpuconfigurator.model.category.CategoryConfigOption;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.model.enums.AttributeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attribute")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String attributeName;


    @OneToMany(mappedBy="attribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AttributeOption> attributeOptions;


    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CategoryConfigOption> categoryConfigOptions;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}

package com.amalitech.gpuconfigurator.model.attributes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    @Column
    private boolean isMeasured = false;

    @Column(name = "unit")
    private String unit;

    @Column
    private String description;

    @Column
    private boolean isRequired;

    @JsonIgnore
    @OneToMany(mappedBy="attribute", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AttributeOption> attributeOptions;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return isMeasured == attribute.isMeasured && isRequired == attribute.isRequired && Objects.equals(id, attribute.id)
                && Objects.equals(attributeName, attribute.attributeName) && Objects.equals(unit, attribute.unit)
                && Objects.equals(description, attribute.description) && Objects.equals(attributeOptions, attribute.attributeOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attributeName, isMeasured, unit, description, isRequired, attributeOptions);
    }
}

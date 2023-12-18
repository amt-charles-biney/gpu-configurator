package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.enums.AttributeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    private AttributeType attributeType;

    @OneToMany(mappedBy="attribute", cascade = CascadeType.ALL)
    private List<AttributeOption> attributeOptions;

}

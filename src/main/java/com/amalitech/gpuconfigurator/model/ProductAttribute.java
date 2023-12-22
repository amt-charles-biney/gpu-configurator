package com.amalitech.gpuconfigurator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productAttribute")
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_options_id", nullable = false)
    private AttributeOption attributeOption;

}

package com.amalitech.gpuconfigurator.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ConfiguredProduct {
    private String name;
    private BigDecimal price;
    private String coverImage;
    private String configurationName;
    private BigDecimal configurationPrice;
    private String configurationSize;
}

package com.amalitech.gpuconfigurator.model.configuration;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Configuration_Options")
@Table(name = "configuration_options")
public class ConfigOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_type")
    private String optionType;

    @Column(name = "option_price")
    private BigDecimal optionPrice;

    @Column(name = "is_included")
    private boolean isIncluded;



    @Column(name = "base_amount")
    private BigDecimal baseAmount;

    @Column(name = "size")
    private String size;

}

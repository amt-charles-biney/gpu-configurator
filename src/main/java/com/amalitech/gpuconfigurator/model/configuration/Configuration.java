package com.amalitech.gpuconfigurator.model.configuration;

import com.amalitech.gpuconfigurator.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Configuration")
@Table(name = "configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "product_id_fk")
    )
    private Product product;


    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<ConfigOptions> configuredOptions = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}

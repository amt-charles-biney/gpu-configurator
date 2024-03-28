package com.amalitech.gpuconfigurator.model.configuration;

import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.WishList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Configuration")
@Table(name = "configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
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
    @ToString.Exclude
    private List<ConfigOptions> configuredOptions = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private WishList wishList;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private int quantity;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String configurationUrl;

    public String getConfigurationUrl() {
//      /product/configure/{productId}?warranty={bool}&components={[variantIds underscore size info for measured variants]}

        StringBuilder path = new StringBuilder("/product/configure/");

        path.append(this.getProduct().getId());

        path.append("?warranty=false&components=");

        List<String> components = this.getConfiguredOptions().stream().map(option ->
                option.getOptionId() + "_" + (option.getIsMeasured() && !option.getSize().isBlank() ? option.getSize() : "0")).toList();
        path.append(String.join(",", components));

        return path.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Configuration that = (Configuration) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package com.amalitech.gpuconfigurator.model.attributes;


import com.amalitech.gpuconfigurator.model.CompatibleOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Entity
@Table(name = "attribute_options")
public class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "price_adjustment")
    private BigDecimal priceAdjustment;

    @Column(name = "media")
    private String media;

    @Column(name = "brand")
    private String brand;

    @Column(name="base_amount")
    private Float baseAmount;

    @Column(name="max_amount")
    private Float maxAmount;

    @Column(name="price_increment")
    private Double priceFactor;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Attribute attribute;

    private Integer inStock;

    @OneToMany(mappedBy = "attributeOption", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CompatibleOption> compatibleOptions;

    @Column(name = "incompatible_attribute_option")
    private List<UUID> incompatibleAttributeOptions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AttributeOption that = (AttributeOption) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package com.amalitech.gpuconfigurator.model.configuration;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Column(name = "is_measured")
    private Boolean isMeasured;

    @Column(name = "base_amount")
    private BigDecimal baseAmount;

    @Column(name = "size")
    private String size;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ConfigOptions that = (ConfigOptions) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

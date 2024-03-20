package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "wish_lists")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserSession userSession;

    @OneToMany(mappedBy = "wishList")
    private Set<Configuration> configuredProducts;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishList wishList = (WishList) o;
        return Objects.equals(id, wishList.id) && Objects.equals(configuredProducts, wishList.configuredProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configuredProducts);
    }
}

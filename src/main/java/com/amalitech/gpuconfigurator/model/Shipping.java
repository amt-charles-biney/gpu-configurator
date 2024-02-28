package com.amalitech.gpuconfigurator.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shippings")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address1;

    @Column
    private String address2;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zipCode;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(nullable = false)
    private Contact contact;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipping shipping = (Shipping) o;
        return Objects.equals(id, shipping.id) && Objects.equals(firstName, shipping.firstName) && Objects.equals(lastName, shipping.lastName)
                && Objects.equals(address1, shipping.address1) && Objects.equals(address2, shipping.address2) && Objects.equals(country, shipping.country)
                && Objects.equals(state, shipping.state) && Objects.equals(city, shipping.city) && Objects.equals(zipCode, shipping.zipCode) && Objects.equals(contact, shipping.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address1, address2, country, state, city, zipCode, contact);
    }
}

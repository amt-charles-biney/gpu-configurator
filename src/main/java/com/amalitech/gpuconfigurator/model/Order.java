package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Order")
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "tracking_id", nullable = false)
    private String tracking_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne
    private Cart cart;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) && getStatus() == order.getStatus() && Objects.equals(getUser(), order.getUser()) && Objects.equals(getPayment(), order.getPayment()) && Objects.equals(getCart(), order.getCart()) && Objects.equals(getCreatedAt(), order.getCreatedAt()) && Objects.equals(getUpdatedAt(), order.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus(), getUser(), getPayment(), getCart(), getCreatedAt(), getUpdatedAt());
    }

}
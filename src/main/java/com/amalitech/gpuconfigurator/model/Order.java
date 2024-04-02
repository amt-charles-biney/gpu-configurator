package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "status")
    private String status;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Column(name = "estimated_delivery")
    private String estDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_session")
    private UserSession userSession;

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne
    private Cart cart;


    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getId(), order.getId()) && Objects.equals(getStatus(), order.getStatus()) && Objects.equals(getTracking_id(), order.getTracking_id()) && Objects.equals(getUser(), order.getUser()) && Objects.equals(getPayment(), order.getPayment()) && Objects.equals(getCart(), order.getCart()) && Objects.equals(getCreatedAt(), order.getCreatedAt()) && Objects.equals(getUpdatedAt(), order.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus(), getTracking_id(), getUser(), getPayment(), getCart(), getCreatedAt(), getUpdatedAt());
    }
}
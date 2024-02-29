package com.amalitech.gpuconfigurator.model;

import com.amalitech.gpuconfigurator.model.payment.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderType status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @ToString.Exclude
    private Payment payments;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private OrderProduct orderProduct;
}

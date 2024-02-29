package com.amalitech.gpuconfigurator.model.payment;

import com.amalitech.gpuconfigurator.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ref", unique = true, nullable = false)
    private String ref;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "channel", nullable = false)
    private String channel;

    @Column(name = "currency", nullable = false)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) && Objects.equals(getUser(), payment.getUser()) && Objects.equals(getRef(), payment.getRef()) && Objects.equals(getAmount(), payment.getAmount()) && Objects.equals(getChannel(), payment.getChannel()) && Objects.equals(getCurrency(), payment.getCurrency()) && Objects.equals(getCreatedAt(), payment.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getRef(), getAmount(), getChannel(), getCurrency(), getCreatedAt());
    }
}

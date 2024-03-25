package com.amalitech.gpuconfigurator.repository.payment;

import com.amalitech.gpuconfigurator.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByRef(String reference);

    @Query("SELECT SUM(u.amount) FROM Payment u")
    BigDecimal revenue();
}

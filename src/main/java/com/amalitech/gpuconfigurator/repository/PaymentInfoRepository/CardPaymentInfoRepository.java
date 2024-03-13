package com.amalitech.gpuconfigurator.repository.PaymentInfoRepository;

import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardPaymentInfoRepository extends JpaRepository<CardPayment, UUID> {
    List<CardPayment> findAllByUser(User user);
    Optional<CardPayment> findOneByIdAndUser(UUID uuid, User user);
}

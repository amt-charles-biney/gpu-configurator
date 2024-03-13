package com.amalitech.gpuconfigurator.repository.PaymentInfoRepository;

import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MobileMoneyInfoRepository extends JpaRepository<MobilePayment, UUID> {

    List<MobilePayment> findAllByUser(User user);

    Optional<MobilePayment> findOneByIdAndUser(UUID uuid, User user);
}

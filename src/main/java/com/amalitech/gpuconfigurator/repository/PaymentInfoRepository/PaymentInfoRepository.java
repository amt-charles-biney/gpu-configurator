package com.amalitech.gpuconfigurator.repository.PaymentInfoRepository;

import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfo;
import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfoType;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfoType, UUID> {
    List<PaymentInfoType> findAllByPaymentTypeAndUser(PaymentInfo paymentInfo, User user);
}

package com.amalitech.gpuconfigurator.service.PaymentInfo;

import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PaymentInfoService {
    List<MobileMoneyResponse> getAllMobileMoneyPayments();

    MobileMoneyResponse saveMobileMoneyPayment(String phoneNumber);

    List<CardInfoResponse> getAllCardPayments();

    CardInfoResponse saveCardPayment(CardInfoRequest cardInfoRequest);

}

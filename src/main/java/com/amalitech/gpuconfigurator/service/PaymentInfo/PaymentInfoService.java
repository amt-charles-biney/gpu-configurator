package com.amalitech.gpuconfigurator.service.PaymentInfo;

import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface PaymentInfoService {
    Optional<MobilePayment> getOneMobileMoneyPayment();

    Optional<CardPayment> getOneCardPayment();

    MobileMoneyResponse saveMobileMoneyPayment(MobileMoneyRequest mobileMoneyRequest);

    CardInfoResponse saveCardPayment(CardInfoRequest cardInfoRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}

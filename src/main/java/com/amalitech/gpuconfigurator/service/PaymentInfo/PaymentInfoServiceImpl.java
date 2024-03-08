package com.amalitech.gpuconfigurator.service.PaymentInfo;

import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfo;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentInfoServiceImpl implements PaymentInfoService{

    private final PaymentInfoRepository paymentInfoRepository;

    @Override
    public List<MobileMoneyResponse> getAllMobileMoneyPayments() {
        List<MobilePayment> mobilePayments = paymentInfoRepository.findAllByMobilePayment(PaymentInfo.MOBILE_MONEY);
        return mobilePayments.stream()
                .map(payment -> new MobileMoneyResponse(payment.getPhoneNumber(), payment.getId().toString()))
                .toList();
    }

    @Override
    public MobileMoneyResponse saveMobileMoneyPayment(String phoneNumber) {
        MobilePayment paymentInfoType = new MobilePayment();
        paymentInfoType.setPaymentType(PaymentInfo.MOBILE_MONEY);
        MobilePayment mobilePayment = paymentInfoRepository.save(paymentInfoType);

        return new MobileMoneyResponse(mobilePayment.getPhoneNumber(), mobilePayment.getId().toString());
    }

    @Override
    public List<CardInfoResponse> getAllCardPayments() {
        return paymentInfoRepository.findAllByCardPayment(PaymentInfo.CARD).stream()
                .map(payment -> CardInfoResponse
                        .builder()
                        .cardHolderName(payment.getCardholderName())
                        .cardNumber(payment.getCardNumber())
                        .expirationDate(payment.getExpirationDate())
                        .build())
                .toList();
    }

    @Override
    public CardInfoResponse saveCardPayment(CardInfoRequest cardInfoRequest) {
        CardPayment cardPayment = new CardPayment();
        cardPayment.setPaymentType(PaymentInfo.CARD);
        CardPayment savedCardPayment = paymentInfoRepository.save(cardPayment);

        return CardInfoResponse
                .builder()
                .id(savedCardPayment.getId().toString())
                .cardHolderName(savedCardPayment.getCardholderName())
                .expirationDate(savedCardPayment.getExpirationDate())
                .cardNumber(savedCardPayment.getCardNumber())
                .build();
    }
}

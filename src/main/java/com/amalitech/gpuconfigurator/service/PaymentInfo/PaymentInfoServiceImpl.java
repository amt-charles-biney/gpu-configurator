package com.amalitech.gpuconfigurator.service.PaymentInfo;

import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfo;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.CardPaymentInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.MobileMoneyInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.PaymentInfoRepository;
import com.amalitech.gpuconfigurator.service.encryption.AesEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentInfoServiceImpl implements PaymentInfoService{

    private final PaymentInfoRepository paymentInfoRepository;
    private final CardPaymentInfoRepository cardPaymentInfoRepository;
    private final MobileMoneyInfoRepository mobileMoneyInfoRepository;
    private final AesEncryptionService encryptionService;

    @Override
    public Optional<MobilePayment> getOneMobileMoneyPayment() {
        User user = getCurrentUserHelper();
       return mobileMoneyInfoRepository.findOneByUser(user);
    }

    @Override
    public Optional<CardPayment> getOneCardPayment() {
        User user = getCurrentUserHelper();
        return cardPaymentInfoRepository.findOneByUser(user);
    }

    @Override
    public MobileMoneyResponse saveMobileMoneyPayment(@Validated MobileMoneyRequest mobileMoneyRequest) {
        User user = getCurrentUserHelper();
        MobilePayment paymentInfoType = mobileMoneyInfoRepository.findOneByUser(user).orElseGet(() -> new MobilePayment());

        paymentInfoType.setPhoneNumber(mobileMoneyRequest.phoneNumber());
        paymentInfoType.setPaymentType(PaymentInfo.MOBILE_MONEY);
        paymentInfoType.setNetwork(mobileMoneyRequest.network());
        paymentInfoType.setUser(user);

        MobilePayment mobilePayment = paymentInfoRepository.save(paymentInfoType);

        return MobileMoneyResponse
                .builder()
                .network(mobilePayment.getNetwork())
                .phoneNumber(mobilePayment.getPhoneNumber())
                .id(mobilePayment.getId().toString())
                .build();
    }

    @Override
    public CardInfoResponse saveCardPayment(CardInfoRequest cardInfoRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = getCurrentUserHelper();

        CardPayment cardPayment = cardPaymentInfoRepository.findOneByUser(user).orElseGet(() -> new CardPayment());
        cardPayment.setPaymentType(PaymentInfo.CARD);

        String encryptedCardNumber = encryptionService.encrypt(cardInfoRequest.cardNumber());
        String encryptedCardHolderName = encryptionService.encrypt(cardInfoRequest.cardHolderName());
        String encryptedCardExpiry = encryptionService.encrypt(cardInfoRequest.expirationDate());

        cardPayment.setCardNumber(encryptedCardNumber);
        cardPayment.setCardholderName(encryptedCardHolderName);
        cardPayment.setExpirationDate(encryptedCardExpiry);
        cardPayment.setUser(user);

        CardPayment savedCardPayment = paymentInfoRepository.save(cardPayment);

        return CardInfoResponse
                .builder()
                .id(savedCardPayment.getId().toString())
                .cardholderName(encryptionService.decrypt(savedCardPayment.getCardholderName()))
                .expirationDate(encryptionService.decrypt(savedCardPayment.getExpirationDate()))
                .cardNumber(encryptionService.decrypt(savedCardPayment.getCardNumber()))
                .build();
    }

    public User getCurrentUserHelper() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.isAuthenticated())
            throw new IllegalArgumentException("No authenticated user found");

        return (User) authentication.getPrincipal();
    }
}

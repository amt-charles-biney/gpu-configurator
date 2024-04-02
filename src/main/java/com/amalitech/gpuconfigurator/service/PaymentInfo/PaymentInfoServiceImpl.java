package com.amalitech.gpuconfigurator.service.PaymentInfo;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.*;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfo;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.CardPaymentInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.MobileMoneyInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.PaymentInfoRepository;
import com.amalitech.gpuconfigurator.service.encryption.AesEncryptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final CardPaymentInfoRepository cardPaymentInfoRepository;
    private final MobileMoneyInfoRepository mobileMoneyInfoRepository;
    private final AesEncryptionService encryptionService;

    @Override
    public List<MobileMoneyResponse> getAllMobilePaymentByUser() {
        User user = getCurrentUserHelperOrNull();

        if(user == null) return Collections.emptyList();

        List<MobilePayment> mobilePayment = mobileMoneyInfoRepository.findAllByUser(user);
        if(mobilePayment.isEmpty()) return Collections.emptyList();

        return mobilePayment
                .stream()
                .map(this::buildMobileMoneyResponse)
                .toList();
    }

    @Override
    public List<CardInfoResponse> getAllCardPaymentByUser() {
        User user = getCurrentUserHelperOrNull();

        if(user == null) return Collections.emptyList();

        List<CardPayment> cardPayments = cardPaymentInfoRepository.findAllByUser(user);

        if(cardPayments.isEmpty()) return Collections.emptyList();

        return cardPayments
                .stream()
                .map(this::mapToCardInfoResponse)
                .toList();
    }

    @Override
    public MobileMoneyResponse saveMobileMoneyPayment(@Validated MobileMoneyRequest mobileMoneyRequest) {
        MobilePayment paymentInfoType = getMobilePayment(mobileMoneyRequest);
        MobilePayment mobilePayment = paymentInfoRepository.save(paymentInfoType);

        return this.buildMobileMoneyResponse(mobilePayment);
    }


    @Override
    public CardInfoResponse saveCardPayment(CardInfoRequest cardInfoRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = getCurrentUserHelper();

        CardPayment cardPayment = new CardPayment();
        cardPayment.setPaymentType(PaymentInfo.CARD);

        String encryptedCardNumber = encryptionService.encrypt(cardInfoRequest.cardNumber());
        String encryptedCardHolderName = encryptionService.encrypt(cardInfoRequest.cardHolderName());
        String encryptedCardExpiry = encryptionService.encrypt(cardInfoRequest.expirationDate());

        cardPayment.setCardNumber(encryptedCardNumber);
        cardPayment.setCardholderName(encryptedCardHolderName);
        cardPayment.setExpirationDate(encryptedCardExpiry);
        cardPayment.setPaymentMethod(cardInfoRequest.paymentMethod());
        cardPayment.setUser(user);

        CardPayment savedCardPayment = paymentInfoRepository.save(cardPayment);

        return this.buildCardInfoResponse(savedCardPayment);

    }

    @Override
    public GenericResponse deletePaymentInfo(String id) {
        paymentInfoRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(200, "deleted payment info successfully");
    }

    @Override
    public MobileMoneyResponse getOneMobileMoneyPaymentInfo(String id) {
        User user = getCurrentUserHelper();
        MobilePayment mobilePayment = mobileMoneyInfoRepository
                .findOneByIdAndUser(UUID.fromString(id), user)
                .orElseThrow(() -> new EntityNotFoundException("could not find mobile money info"));

        return this.buildMobileMoneyResponse(mobilePayment);
    }

    @Override
    public CardInfoResponse getOneCardPaymentInfo(String id) throws InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = getCurrentUserHelper();
        CardPayment cardPayment = cardPaymentInfoRepository
                .findOneByIdAndUser(UUID.fromString(id), user)
                .orElseThrow(() -> new EntityNotFoundException("could not find card info"));

        return this.buildCardInfoResponse(cardPayment);
    }


    private MobilePayment getMobilePayment(MobileMoneyRequest mobileMoneyRequest) {
        User user = getCurrentUserHelper();
        MobilePayment paymentInfoType = new MobilePayment();

        paymentInfoType.setPhoneNumber(mobileMoneyRequest.contact().phoneNumber());
        paymentInfoType.setPaymentType(PaymentInfo.MOBILE_MONEY);
        paymentInfoType.setNetwork(mobileMoneyRequest.network());
        paymentInfoType.setCountry(mobileMoneyRequest.contact().country());
        paymentInfoType.setDialCode(mobileMoneyRequest.contact().dialCode());
        paymentInfoType.setIso2Code(mobileMoneyRequest.contact().iso2Code());
        paymentInfoType.setUser(user);

        return paymentInfoType;
    }

    private MobileMoneyResponse buildMobileMoneyResponse(MobilePayment mobilePayment) {
        return MobileMoneyResponse
                .builder()
                .network(mobilePayment.getNetwork())
                .contact(ContactRequest
                        .builder()
                        .country(mobilePayment.getCountry())
                        .dialCode(mobilePayment.getDialCode())
                        .iso2Code(mobilePayment.getIso2Code())
                        .phoneNumber(mobilePayment.getPhoneNumber())
                        .build())
                .id(mobilePayment.getId().toString())
                .build();
    }

    private CardInfoResponse buildCardInfoResponse(CardPayment cardPayment) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        return CardInfoResponse
                .builder()
                .id(cardPayment.getId().toString())
                .cardHolderName(encryptionService.decrypt(cardPayment.getCardholderName()))
                .expirationDate(encryptionService.decrypt(cardPayment.getExpirationDate()))
                .cardNumber(encryptionService.decrypt(cardPayment.getCardNumber()))
                .paymentMethod(cardPayment.getPaymentMethod())
                .build();
    }


    private CardInfoResponse mapToCardInfoResponse(CardPayment cardPayment) {
        try {
            return this.buildCardInfoResponse(cardPayment);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Error occurred while processing card payment info", e);
        }
    }


    public User getCurrentUserHelper() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated())
            throw new IllegalArgumentException("No authenticated user found");

        return (User) authentication.getPrincipal();
    }

    public User getCurrentUserHelperOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return (User) authentication.getPrincipal();
        } else {
            return null;
        }
    }
}

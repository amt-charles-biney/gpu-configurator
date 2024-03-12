package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.PaymentInfo;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.CardPaymentInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.MobileMoneyInfoRepository;
import com.amalitech.gpuconfigurator.repository.PaymentInfoRepository.PaymentInfoRepository;
import com.amalitech.gpuconfigurator.service.PaymentInfo.PaymentInfoServiceImpl;
import com.amalitech.gpuconfigurator.service.encryption.AesEncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PaymentInfoServiceTest {

    @Mock
    PaymentInfoRepository paymentInfoRepository;

    @Mock
    CardPaymentInfoRepository cardPaymentInfoRepository;

    @Mock
    MobileMoneyInfoRepository mobileMoneyInfoRepository;

    @Mock
    AesEncryptionService encryptionService;

    @InjectMocks
    PaymentInfoServiceImpl paymentInfoService;

    private User user;
    private UUID userId;

    private UUID cardPaymentId;
    private CardPayment cardPayment;
    private MobilePayment mobilePayment;
    private UUID mobilePaymentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userId = UUID.randomUUID();

        user = User
                .builder()
                .id(userId)
                .email("test@gmail.com")
                .role(Role.ADMIN)
                .build();

        cardPaymentId = UUID.randomUUID();
        cardPayment = new CardPayment();

        cardPayment.setId(cardPaymentId);
        cardPayment.setPaymentType(PaymentInfo.CARD);
        cardPayment.setUser(user);
        cardPayment.setCardNumber("XXX-XXX-XXX");
        cardPayment.setCardholderName("dickson anyaele");
        cardPayment.setExpirationDate("12-01-4");

        mobilePaymentId = UUID.randomUUID();

        mobilePayment = new MobilePayment();

        mobilePayment.setId(mobilePaymentId);
        mobilePayment.setPaymentType(PaymentInfo.MOBILE_MONEY);
        mobilePayment.setUser(user);
        mobilePayment.setPhoneNumber("0248267980");

        mockAuthenticatedUser();

    }

    private void mockAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void testGetOneMobileMoneyPayments() {
        when(mobileMoneyInfoRepository.findOneByUser(any())).thenReturn(Optional.ofNullable(mobilePayment));

        Optional<MobilePayment> result = paymentInfoService.getOneMobileMoneyPayment();

        assertNotNull(result);
        verify(mobileMoneyInfoRepository, times(1)).findOneByUser(any());
    }


    @Test
    void testGetAllCardPayments() {
        when(cardPaymentInfoRepository.findOneByUser(any())).thenReturn(Optional.ofNullable(cardPayment));

        Optional<CardPayment> result = paymentInfoService.getOneCardPayment();

        assertNotNull(result);
        verify(cardPaymentInfoRepository, times(1)).findOneByUser(any());
    }

    @Test
    void testSaveMobileMoneyPayment() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String mobileNumber = "0248267980";
        String mobileNetwork = "MTN";
        UUID mobilePaymentId = UUID.randomUUID();

        MobileMoneyRequest mobileMoneyRequest = new MobileMoneyRequest(mobileNumber, mobileNetwork);
        MobilePayment mobilePayment = new MobilePayment();


        mobilePayment.setId(mobilePaymentId);
        mobilePayment.setUser(user);
        mobilePayment.setPhoneNumber(mobileNumber);
        mobilePayment.setNetwork(mobileNetwork);

        when(paymentInfoRepository.save(any())).thenReturn(mobilePayment);
        when(encryptionService.encrypt(any(String.class))).thenReturn("encrypted");

        MobileMoneyResponse result = paymentInfoService.saveMobileMoneyPayment(mobileMoneyRequest);

        assertNotNull(result);
        verify(paymentInfoRepository, times(1)).save(any());
    }

    @Test
    void testSaveCardPayment() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Mocking
        CardInfoRequest cardInfoRequest = CardInfoRequest
                .builder()
                .cardHolderName("decrypted")
                .cardNumber("decrypted")
                .build();

        when(encryptionService.encrypt(any())).thenReturn("encrypted");
        when(encryptionService.decrypt(any())).thenReturn("decrypted");

        cardPayment.setCardNumber(encryptionService.encrypt(cardPayment.getCardNumber()));
        cardPayment.setExpirationDate(encryptionService.encrypt(cardPayment.getExpirationDate()));
        when(paymentInfoRepository.save(any())).thenReturn(cardPayment);

        CardInfoResponse result = paymentInfoService.saveCardPayment(cardInfoRequest);

        assertEquals("decrypted", result.cardNumber());
        assertEquals("decrypted", result.cardholderName());
        verify(paymentInfoRepository, times(1)).save(any());
    }

    @Test
    void testGetCurrentUserHelper() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);


        paymentInfoService.getCurrentUserHelper();

        verify(securityContext, times(1)).getAuthentication();
    }
}

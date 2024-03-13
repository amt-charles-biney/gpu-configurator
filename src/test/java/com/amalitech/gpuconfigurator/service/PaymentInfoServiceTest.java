package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.*;
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
import java.util.List;
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
    private CardPayment cardPayment = new CardPayment();
    private MobilePayment mobilePayment = new MobilePayment();
    private UUID mobilePaymentId;
    private CardInfoResponse cardInfoResponse;

    private ContactRequest contact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mobilePaymentId = UUID.randomUUID();
        userId = UUID.randomUUID();
        cardPaymentId = UUID.randomUUID();

        user = User
                .builder()
                .id(userId)
                .email("test@gmail.com")
                .role(Role.ADMIN)
                .build();

        contact = ContactRequest
                .builder()
                .phoneNumber("0248267980")
                .iso2Code("233")
                .dialCode("323")
                .country("ghana")
                .build();

        cardInfoResponse = CardInfoResponse
                .builder()
                .cardHolderName("name")
                .expirationDate("12/3")
                .cardNumber("xxx-tentaction")
                .build();


        cardPayment.setId(cardPaymentId);
        cardPayment.setPaymentType(PaymentInfo.CARD);
        cardPayment.setUser(user);
        cardPayment.setCardNumber("1234567891054123");
        cardPayment.setCardholderName("dickson anyaele");
        cardPayment.setExpirationDate("12-01-4");

        mobilePayment.setId(mobilePaymentId);
        mobilePayment.setPaymentType(PaymentInfo.MOBILE_MONEY);
        mobilePayment.setUser(user);
        mobilePayment.setIso2Code(contact.iso2Code());
        mobilePayment.setCountry(contact.country());
        mobilePayment.setDialCode(contact.dialCode());
        mobilePayment.setNetwork("MTN");
        mobilePayment.setPhoneNumber(contact.phoneNumber());

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
    void testGetAllMobileMoneyByUser_shouldReturnAllMobileMoneyInfo() {
        List<MobilePayment> mobilePayments = List.of(mobilePayment);
        when(mobileMoneyInfoRepository.findAllByUser(any())).thenReturn(mobilePayments);

        List<MobileMoneyResponse> result = paymentInfoService.getAllMobilePaymentByUser();

        assertNotNull(result);
        assertEquals(result.size(), mobilePayments.size());
        verify(mobileMoneyInfoRepository, times(1)).findAllByUser(any());
    }

    /*
    @Test
    void testGetOneMobileMoneyByAuthenticatedUser_shouldReturnValidInfo() {
        MobilePayment result =  new MobilePayment();
        mobilePayment.setId(UUID.randomUUID());
        when(mobileMoneyInfoRepository.findOneByIdAndUser(any(UUID.class), any(User.class))).thenReturn(Optional.ofNullable(mobilePayment));

        MobileMoneyResponse response = paymentInfoService.getOneMobileMoneyPaymentInfo(String.valueOf(mobilePaymentId));

        assertNotNull(response);
        assertEquals(response.id(), mobilePayment.getId().toString());
        verify(mobileMoneyInfoRepository, times(1)).findOneByIdAndUser(any(UUID.class), any(User.class));
    }

    @Test
    void testGetOneCardPaymentInfoByAuthenticatedUser_shouldReturnValidInfo() throws InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        when(cardPaymentInfoRepository.findOneByIdAndUser(cardPaymentId, cardPayment.getUser())).thenReturn(Optional.ofNullable(cardPayment));

        CardInfoResponse response = paymentInfoService.getOneCardPaymentInfo(mobilePaymentId.toString());

        assertNotNull(response);
        assertEquals(response.id(), cardPayment.getId().toString());
        verify(cardPaymentInfoRepository, times(1)).findOneByIdAndUser(any(UUID.class), any(User.class));
    }
*/


    @Test
    void testGetAllCardPayments() {
        when(cardPaymentInfoRepository.findAllByUser(any())).thenReturn(List.of(cardPayment));

        List<CardInfoResponse> result = paymentInfoService.getAllCardPaymentByUser();

        assertNotNull(result);
        assertEquals(result.size(), List.of(cardPayment).size());
        verify(cardPaymentInfoRepository, times(1)).findAllByUser(any());
    }

    @Test
    void testSaveMobileMoneyPayment() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String mobileNetwork = "MTN";

        MobileMoneyRequest mobileMoneyRequest = new MobileMoneyRequest(contact, mobileNetwork);

        when(paymentInfoRepository.save(any())).thenReturn(mobilePayment);

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

        assertNotNull(result);
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

    @Test
    void testDeletePaymentInfo() {

        doNothing().when(paymentInfoRepository).deleteById(any(UUID.class));

        GenericResponse result = paymentInfoService.deletePaymentInfo(String.valueOf(mobilePaymentId));

        assertNotNull(result);
        verify(paymentInfoRepository, times(1)).deleteById(any(UUID.class));
    }
}

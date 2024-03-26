package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private OtpServiceImpl otpService;

    private String email = "testOtp1234@gmail.com";
    private OtpType otpType = OtpType.CREATE;
    private Otp otp = new Otp();
    private UUID otpId;
    private String otpCode = "123456";
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        otpId = UUID.randomUUID();

        otp.setEmail(email);
        otp.setId(otpId);
    }

    @Test
    public void testGenerateOtp() {
        String otp = otpService.generateOtp();
        assertNotNull(otp);
        assertEquals(6, otp.length());
    }

    @Test
    public void testIsValidOtp_ValidOtp() {

        otp.setCode(otpCode);
        otp.setExpiration(LocalDateTime.now().plusMinutes(5));

        when(otpRepository.findByEmailAndCodeAndType(email, otpCode, otpType)).thenReturn(Optional.of(otp));

        assertTrue(otpService.isValidOtp(email, otpCode, otpType));
        verify(otpRepository, times(1)).findByEmailAndCodeAndType(email, otpCode, otpType);
    }

    @Test
    public void testIsValidOtp_ExpiredOtp() {

        otp.setCode(otpCode);
        otp.setExpiration(LocalDateTime.now().minusMinutes(1));

        when(otpRepository.findByEmailAndCodeAndType(email, otpCode, otpType)).thenReturn(Optional.of(otp));

        assertFalse(otpService.isValidOtp(email, otpCode, otpType));
        verify(otpRepository, times(1)).findByEmailAndCodeAndType(email, otpCode, otpType);
    }

    @Test
    public void testIsExpiredOtp_NotExpired() {
        otp.setExpiration(LocalDateTime.now().plusMinutes(1));
        assertFalse(otpService.isExpiredOtp(otp));
    }

    @Test
    public void testIsExpiredOtp_Expired() {
        otp.setExpiration(LocalDateTime.now().minusMinutes(1));
        assertTrue(otpService.isExpiredOtp(otp));
    }

    @Test
    public void testGenerateAndSaveOtp() {
        User user = new User();
        String code = otpService.generateAndSaveOtp(user, otpType);
        assertNotNull(code);
        verify(otpRepository, times(1)).save(any(Otp.class));
    }

    @Test
    public void testDeleteOtp() {
        otpService.deleteOtp(email, otpCode);
        verify(otpRepository, times(1)).deleteByEmailAndCode(email, otpCode);
    }

}

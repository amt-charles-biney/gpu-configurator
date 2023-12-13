package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import com.amalitech.gpuconfigurator.service.otpService.OtpService;
import com.amalitech.gpuconfigurator.service.otpService.OtpServiceImpl;
import lombok.RequiredArgsConstructor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpServiceTest {

    @InjectMocks
    private OtpServiceImpl otpService;

    @Mock
    private OtpRepository otpRepository;


    @Test
    public void generateOtp_shouldGenerateNonEmptyString() {
        String otp = otpService.generateOtp();

        assertNotNull(otp);
        assertFalse(otp.isEmpty());
    }


    @Test
    public void generateAndSaveOtp_shouldGenerateAndSaveOtp() {
        String userId = "550e8400-e29b-41d4-a716-446655440000";
        var user = User.builder()
                        .id(UUID.fromString(userId))
                        .firstName("dickson")
                        .lastName("anyaele")
                        .email("kaycydickson@gmail.com")
                        .password("dissonance")
                        .isVerified(false)
                        .role(Role.USER).build();

        String code = otpService.generateAndSaveOtp(user, OtpType.CREATE);

        assertNotNull(code);
        assertFalse(code.isEmpty());
    }

    @Test
    public void isExpiredOtp_shouldPassIfOtpIsNotExpired(){
        var otpInstance = Otp.builder()
                .expiration(LocalDateTime.now().plusMinutes(5))
                .code("1234")
                .email("kaycydicksons@gmail.com")
                .type(OtpType.CREATE)
                .build();

        Boolean isNotExpired = otpService.isExpiredOtp(otpInstance);

        assertFalse(isNotExpired);
    }

    @Test
    public void isExpiredOtp_shouldPassIfOtpIsExpired(){
        var otpInstance = Otp.builder()
                .expiration(LocalDateTime.now().plusMinutes(-5))
                .code("1234")
                .email("kaycydicksons@gmail.com")
                .type(OtpType.CREATE)
                .build();

        Boolean isExpired = otpService.isExpiredOtp(otpInstance);

        assertTrue(isExpired);
    }


}

package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import com.amalitech.gpuconfigurator.service.otpService.OtpService;
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
    private OtpService otpService;

    @Mock
    private OtpRepository otpRepository;

    @Test
    public void generateOtp_shouldGenerateNonEmptyString() {
        String otp = otpService.generateOtp();

        assertNotNull(otp);
        assertFalse(otp.isEmpty());
    }

   /*
    @Test
    public void saveOtp_shouldSaveOtp() {
        var otpInstance = Otp.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")).email("testemail@gmail.com").code("12345").expiration(LocalDateTime.now().plusMinutes(5)).build();
    }
    */

}

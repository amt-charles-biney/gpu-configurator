package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.service.otpService.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
public class OtpServiceTest {

    private final OtpService otpService;

    @Test
    public void generateOtp_shouldGenerateNonEmptyString() {
        String otp = otpService.generateOtp();

        assertNotNull(otp);
        assertFalse(otp.isEmpty());
    }

}

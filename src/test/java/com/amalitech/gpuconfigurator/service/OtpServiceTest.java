package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.service.otpService.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpServiceTest {

    @Autowired
    private OtpService otpService;

    @Test
    public void generateOtp_shouldGenerateNonEmptyString() {
        String otp = otpService.generateOtp();

        assertNotNull(otp);
        assertFalse(otp.isEmpty());
    }

}

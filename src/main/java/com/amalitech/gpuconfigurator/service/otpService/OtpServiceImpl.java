package com.amalitech.gpuconfigurator.service.otpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    @Override
    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }

        return otp.toString();
    }

    @Override
    public Boolean isValidOtp(String otp, String otpReceived) {
        return otp.equals(otpReceived);
    }


    @Override
    public Boolean isExpiredOtp(String otp) {
        LocalDateTime creationTime = LocalDateTime.parse(otp.substring(OTP_LENGTH));
        LocalDateTime currentTime = LocalDateTime.now();

        return ChronoUnit.MINUTES.between(creationTime, currentTime) > OTP_EXPIRY_MINUTES;
    }
}

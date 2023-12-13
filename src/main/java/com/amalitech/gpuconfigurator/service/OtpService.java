package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;

    private final OtpRepository otpRepository;

    public String generateOtpCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }

        return otp.toString();
    }

    public Otp generateOtp(String email, OtpType otpType) {
        var otp = Otp
                .builder()
                .code(generateOtpCode())
                .email(email)
                .type(otpType)
                .expiration(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .build();
        return otpRepository.save(otp);
    }

    public Boolean isValidOtp(String otp, String otpReceived) {
        return otp.equals(otpReceived);
    }

    public Boolean isNonExpiredOtp(Otp otp) {
        LocalDateTime creationTime = otp.getCreatedAt();
        LocalDateTime currentTime = LocalDateTime.now();

        return ChronoUnit.MINUTES.between(creationTime, currentTime) > OTP_EXPIRY_MINUTES;
    }

    public boolean verifyOtp(String email, String otpCode, OtpType otpType) {
        Optional<Otp> optionalOtp = otpRepository.findByCodeAndEmail(otpCode, email);
        if (optionalOtp.isPresent()) {
            var otp = optionalOtp.get();
            return isNonExpiredOtp(otp) && otp.getType().equals(otpType);
        }
        return false;
    }

    public void deleteOtp(String email, String code) {
        otpRepository.deleteByCodeAndEmail(code, email);
    }
}
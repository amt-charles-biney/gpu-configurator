package com.amalitech.gpuconfigurator.service.otpService;

public interface OtpService {

    public String generateOtp();

    public Boolean isValidOtp(String otp, String otpReceived);

    public Boolean isExpiredOtp(String otp);

}

package com.amalitech.gpuconfigurator.service.otpService;

import com.amalitech.gpuconfigurator.model.Otp;

public interface OtpService {

    public String generateOtp();

    public Boolean isValidOtp(String otp, String otpReceived);

    public Boolean isExpiredOtp(Otp otp);

    public void saveOtp(String email, String otp);


}

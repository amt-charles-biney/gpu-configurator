package com.amalitech.gpuconfigurator.service.otpService;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.User;

public interface OtpService {

    public String generateOtp();

    public Boolean isValidOtp(String otp, String otpReceived);

    public Boolean isExpiredOtp(Otp otp);

    public String generateAndSaveOtp(User user, OtpType type);

}

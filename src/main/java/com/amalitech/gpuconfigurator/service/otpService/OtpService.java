package com.amalitech.gpuconfigurator.service.otpService;

public interface OtpService {


    /**
     * Generates a cryptographically secure One-Time Password (OTP) of a specified length.
     *
     * @return A randomly generated OTP as a string.
     */
    public String generateOtp();

    /**
     * Validates if the provided OTP matches the OTP received.
     *
     * @param otp         The original OTP that was generated and sent to the user for validation.
     * @param otpReceived The OTP entered or received from the user for validation.
     * @return True if the provided OTP matches the received OTP, false otherwise.
     */
    public Boolean isValidOtp(String otp, String otpReceived);


    /**
     * Checks if the provided OTP has expired based on a predefined expiry time.
     *
     * @param otp The OTP to be checked for expiry. It should include the creation time as part of its content.
     * @return True if the OTP has expired, false otherwise.
     */
    public Boolean isExpiredOtp(String otp);

}

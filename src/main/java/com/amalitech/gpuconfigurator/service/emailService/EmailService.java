package com.amalitech.gpuconfigurator.service.emailService;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendOtpMessage(String to, String otp) throws MessagingException;
    boolean isValidEmail(String email);
}

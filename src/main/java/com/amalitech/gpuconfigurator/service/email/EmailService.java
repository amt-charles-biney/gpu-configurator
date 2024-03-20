package com.amalitech.gpuconfigurator.service.email;

import com.amalitech.gpuconfigurator.dto.email.EmailTemplateRequest;
import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendOtpMessage(String to, String otp, OtpType type) throws MessagingException;
    boolean isValidEmail(String email);

    void sendGenericEmail(EmailTemplateRequest emailTemplateRequest) throws MessagingException;

    void sendOtpMail(String email, String otp);
}

package com.amalitech.gpuconfigurator.service.email;

public interface OrderEmailService {
    void sendEmail(String toEMail, String body, String subject);
}

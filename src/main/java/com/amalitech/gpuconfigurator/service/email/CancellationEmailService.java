package com.amalitech.gpuconfigurator.service.email;

public interface CancellationEmailService {
    void cancelEmail(String toEMail,String body, String subject);
}

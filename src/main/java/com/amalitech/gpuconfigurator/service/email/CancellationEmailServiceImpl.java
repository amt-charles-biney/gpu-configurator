package com.amalitech.gpuconfigurator.service.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancellationEmailServiceImpl implements CancellationEmailService {

    private final JavaMailSender mailSender;
    @Override
    public void cancelEmail(String toEMail, String body, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("S3VERS");
        message.setTo(toEMail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

    }
}

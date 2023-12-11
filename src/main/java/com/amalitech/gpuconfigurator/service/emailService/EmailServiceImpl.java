package com.amalitech.gpuconfigurator.service.emailService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public void sendOtpMessage(String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(mailFrom);
        simpleMailMessage.setText("testing mail");
        simpleMailMessage.setTo(to);

        javaMailSender.send(simpleMailMessage);
    }
}

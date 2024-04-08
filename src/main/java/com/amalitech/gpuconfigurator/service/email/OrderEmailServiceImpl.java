package com.amalitech.gpuconfigurator.service.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEmailServiceImpl implements OrderEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.host}")
    private String sender;

    @Override
    public void sendEmail(String toEMail, String body, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEMail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);


    }
}

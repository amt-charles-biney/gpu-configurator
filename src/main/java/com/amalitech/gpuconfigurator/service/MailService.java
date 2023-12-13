package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;

    public void sendOtpMail(String to, Otp otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp", otp.getCode());
        context.setVariable("otpType", otp.getType());

        String process = templateEngine.process("email-otp-template", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("OTP password for GPU Configurator");
        helper.setText(process, true);
        helper.setTo(to);

        javaMailSender.send(mimeMessage);
    }
}

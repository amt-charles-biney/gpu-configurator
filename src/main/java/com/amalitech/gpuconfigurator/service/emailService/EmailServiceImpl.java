package com.amalitech.gpuconfigurator.service.emailService;

import com.amalitech.gpuconfigurator.model.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;

    @Override
    public void sendOtpMessage(String to, String otp, OtpType type) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp", otp);

        String templateType = type == OtpType.CREATE ? "email-otp-template" : "email-reset-template";
        String process = templateEngine.process(templateType, context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("OTP password for GPU Configurator");
        helper.setText(process, true);
        helper.setTo(to);

        javaMailSender.send(mimeMessage);
    }

    @Override
    public boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }
}

package com.amalitech.gpuconfigurator.service.email;

import com.amalitech.gpuconfigurator.dto.email.OtpTemplate;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class OtpEmailServiceImpl implements EmailService<OtpTemplate>{

    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;
    @Override
    public void send(OtpTemplate message) throws MessagingException {
        this.sendOtpMessage(message.to(), message.otp(), message.otpType());
    }

    private void sendOtpMessage(String to, String otp, OtpType type) throws MessagingException {

        if(!isValidEmail(to)) throw new EntityNotFoundException("email is not valid");
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
}

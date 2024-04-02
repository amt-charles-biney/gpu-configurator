package com.amalitech.gpuconfigurator.service.email;

import com.amalitech.gpuconfigurator.dto.email.EmailOrderRequest;
import com.amalitech.gpuconfigurator.dto.email.EmailTemplateRequest;
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
public class OrderManagementEmailServiceImpl implements EmailService<EmailOrderRequest>{

    private final ITemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    @Override
    public void send(EmailOrderRequest message) throws MessagingException {

        if(!isValidEmail(message.email())) throw new EntityNotFoundException("email is not valid");

        Context context = new Context();
        context.setVariable("message", message.message());

        String process = templateEngine.process("email-order-template", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Order Status");
        helper.setText(process, true);
        helper.setTo(message.email());

        javaMailSender.send(mimeMessage);
    }
}

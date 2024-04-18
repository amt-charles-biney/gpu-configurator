package com.amalitech.gpuconfigurator.service.email;

import com.amalitech.gpuconfigurator.dto.email.EmailStockRequest;
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
public class NotifyStockUpdateEmailServiceImpl implements EmailService<EmailStockRequest>{

    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;
    @Override
    public void send(EmailStockRequest emailStockRequest) throws MessagingException {
        if(!isValidEmail(emailStockRequest.email())) throw new EntityNotFoundException("email is not valid");
        Context context = new Context();
        context.setVariable("message", emailStockRequest.message());
        context.setVariable("productId", emailStockRequest.productId());

        String process = templateEngine.process("email-stock-template", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Product Stock Update");
        helper.setText(process, true);
        helper.setTo(emailStockRequest.email());

        javaMailSender.send(mimeMessage);
    }

}

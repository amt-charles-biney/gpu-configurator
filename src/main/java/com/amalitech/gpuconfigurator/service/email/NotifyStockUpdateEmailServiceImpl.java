package com.amalitech.gpuconfigurator.service.email;

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
public class NotifyStockUpdateEmailServiceImpl implements EmailService<EmailTemplateRequest>{

    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;
    @Override
    public void send(EmailTemplateRequest emailTemplateRequest) throws MessagingException {
        if(!isValidEmail(emailTemplateRequest.to())) throw new EntityNotFoundException("email is not valid");
        Context context = new Context();
        context.setVariable("message", emailTemplateRequest.message());

        String process = templateEngine.process(emailTemplateRequest.templateString(), context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(emailTemplateRequest.title());
        helper.setText(process, true);
        helper.setTo(emailTemplateRequest.to());

        javaMailSender.send(mimeMessage);
    }

}

package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.service.email.EmailService;
import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {


   @InjectMocks
   private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ITemplateEngine templateEngine;

    @Test
    public void isValidEmail_shouldPassIfEmailIsValid() {
        String testEmail = "testemail@gmail.com";
        boolean isValidEmail = emailService.isValidEmail(testEmail);

        assertTrue(isValidEmail);
    }

}

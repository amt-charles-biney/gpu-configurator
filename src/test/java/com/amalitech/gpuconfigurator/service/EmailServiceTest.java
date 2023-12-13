package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

   @Autowired
   @InjectMocks
   private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ITemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    public void isValidEmail_shouldPassIfEmailIsValid() {
        String testEmail = "testemail@gmail.com";
        boolean isValidEmail = emailService.isValidEmail(testEmail);

        assertTrue(isValidEmail);
    }

    /*
    @Test
    public void sendOtpMessage_shouldProcessTemplateAndSendMessage() throws MessagingException {
        String to = "zomlelarke@gufum.com";
        String otp = "123456";

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Processed template");

        emailService.sendOtpMessage(to, otp);

        verify(templateEngine).process(anyString(), any(Context.class));
        verify(javaMailSender).send(any(MimeMessage.class));

        // Implement additional verification/assertions for your email sending logic
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(captor.capture());

        MimeMessage sentMessage = captor.getValue();
        MimeMessageHelper helper = new MimeMessageHelper(sentMessage);
        assertEquals("OTP password for GPU Configurator", helper.getMimeMessage().getSubject());
    }

     */

}

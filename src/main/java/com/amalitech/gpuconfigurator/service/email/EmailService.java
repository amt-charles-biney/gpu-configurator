package com.amalitech.gpuconfigurator.service.email;

import com.amalitech.gpuconfigurator.dto.email.EmailTemplateRequest;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import jakarta.mail.MessagingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface EmailService<T> {

    void send(T message) throws MessagingException;

    default boolean isValidEmail(String email) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

}

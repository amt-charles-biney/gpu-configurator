package com.amalitech.gpuconfigurator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception e){
        ProblemDetail errorDetail = null;
        if(e instanceof BadCredentialsException){

            errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(401));
            errorDetail.setTitle("Bad Credentials");
            errorDetail.setDetail("Invalid email or password");
            errorDetail.setProperty("access_denied_reason", "Invalid Email or Password");
        }

        return errorDetail;
    }
}

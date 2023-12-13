package com.amalitech.gpuconfigurator.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendingException(MessagingException e) {
        String msg = e.getMessage();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorResponse err = new ErrorResponse(msg, statusCode);

        return ResponseEntity.status(statusCode).body(err);
    }
}

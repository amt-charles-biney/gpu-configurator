package com.amalitech.gpuconfigurator.exception;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<Object> handleEmailSendingException(MessagingException e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialException(Exception e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({ UsernameNotFoundException.class, EntityNotFoundException.class })
    public ProblemDetail usernameNotFoundException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(404));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

    @ExceptionHandler(AttributeNameAlreadyExistsException.class)
    public ProblemDetail handleAttributeNameAlreadyExistsException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String detail) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(status);
        errorDetail.setDetail(detail);
        return ResponseEntity.status(status).body(errorDetail);
    }
}

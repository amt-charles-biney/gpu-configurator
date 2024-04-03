package com.amalitech.gpuconfigurator.exception;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice

public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({Exception.class, PaystackErrorException.class})
    public ResponseEntity<Object> handleGenericException(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<Object> handleEmailSendingException(MessagingException e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }


    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialException(Exception e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }


    @ExceptionHandler({UsernameNotFoundException.class, NotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AttributeNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleAttributeNameAlreadyExistsException(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CannotAddItemToCartException.class)
    public ResponseEntity<Object> handleCannotAddItemToCartException(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConfigurationAlreadyInWishListException.class)
    public ResponseEntity<Object> handleConfigurationAlreadyInWishListException(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String detail) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(status);
        errorDetail.setDetail(detail);
        return ResponseEntity.status(status).body(errorDetail);
    }
}

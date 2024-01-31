package com.amalitech.gpuconfigurator.exception;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice

public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<Object> handleEmailSendingException(MessagingException e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, Object> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        errorDetail.setDetail("One or more validation errors occurred");
        errorDetail.setProperty("field_errors", fieldErrors);

        return ResponseEntity.badRequest().body(errorDetail);
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

    private ResponseEntity<Object> buildResponse(HttpStatus status, String detail) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(status);
        errorDetail.setDetail(detail);
        return ResponseEntity.status(status).body(errorDetail);
    }
}

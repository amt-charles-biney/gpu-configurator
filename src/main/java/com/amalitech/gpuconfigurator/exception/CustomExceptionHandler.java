package com.amalitech.gpuconfigurator.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


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
        if(e instanceof DataIntegrityViolationException){
            errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
            errorDetail.setTitle("Data Integrity Violation");
            errorDetail.setDetail("A not-null property references a null or transient value");
        }

        return errorDetail;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

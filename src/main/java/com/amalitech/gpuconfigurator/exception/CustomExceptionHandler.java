package com.amalitech.gpuconfigurator.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class, DataIntegrityViolationException.class, BadRequestException.class, EntityNotFoundException.class})
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail("One or more validation errors occurred");

        Map<String, Object> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        errorDetail.setProperty("field_errors", fieldErrors);
        return errorDetail;
    }


}

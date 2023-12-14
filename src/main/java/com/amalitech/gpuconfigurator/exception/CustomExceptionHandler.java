package com.amalitech.gpuconfigurator.exception;

import com.amalitech.gpuconfigurator.dto.ErrorResponse;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<ErrorResponse> handleEmailSendingException(MessagingException e) {
        String msg = e.getMessage();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorResponse err = new ErrorResponse(msg, statusCode);

        return ResponseEntity.status(statusCode).body(err);
    }


@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
    ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
    errorDetail.setTitle("Validation Failed");
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

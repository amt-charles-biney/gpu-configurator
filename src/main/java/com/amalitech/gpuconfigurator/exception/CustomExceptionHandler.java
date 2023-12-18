package com.amalitech.gpuconfigurator.exception;

import com.amalitech.gpuconfigurator.dto.ErrorResponse;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(401));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dataIntegrityViolationExceptionException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail usernameNotFoundException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(404));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequestException(Exception e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail(e.getMessage());
        return errorDetail;
    }

}

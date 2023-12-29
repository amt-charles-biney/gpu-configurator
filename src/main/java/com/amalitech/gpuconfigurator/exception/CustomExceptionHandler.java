package com.amalitech.gpuconfigurator.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
            BadRequestException.class})
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

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ProblemDetail> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        errorDetail.setDetail("Max file size exceeded");
        errorDetail.setProperty("error_message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }


    @ExceptionHandler({CloudinaryUploadException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleCloudinaryUploadException(CloudinaryUploadException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(500));
        errorDetail.setDetail("Error uploading image to Cloudinary");
        errorDetail.setProperty("error_message", e.getMessage());
        return errorDetail;
    }

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.classgit})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFoundException(NotFoundException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(404));
        errorDetail.setDetail("Not found");
        errorDetail.setProperty("error_message", e.getMessage());
        return errorDetail;
    }

}

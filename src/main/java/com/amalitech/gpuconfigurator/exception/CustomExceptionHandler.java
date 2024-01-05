package com.amalitech.gpuconfigurator.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
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

    private static final String setProperty = "error_message";

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUncheckedExceptions(Exception e) {
        if (e instanceof MaxUploadSizeExceededException ||
                e instanceof BadRequestException ||
                e instanceof DataIntegrityViolationException ||
                e instanceof EntityNotFoundException) {
            return buildProblemDetail(400, e.getMessage(), e.getMessage());
        }
        return buildProblemDetail(500, "Something went wrong", e.getMessage());
    }

    private ProblemDetail buildProblemDetail(int statusCode, String detail, String message) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(statusCode));
        errorDetail.setDetail(detail);
        errorDetail.setProperty(setProperty, message);
        return errorDetail;
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
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

    @ExceptionHandler({CloudinaryUploadException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleCloudinaryUploadException(CloudinaryUploadException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(500));
        errorDetail.setDetail("Error uploading image to Cloudinary");
        errorDetail.setProperty(setProperty, e.getMessage());
        return errorDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFoundException(NotFoundException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(404));
        errorDetail.setDetail(e.getMessage());
        errorDetail.setProperty(setProperty, e.getMessage());
        return errorDetail;
    }
}

package com.amalitech.gpuconfigurator.annotation;

import com.amalitech.gpuconfigurator.validation.OptionalAddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = OptionalAddressValidator.class)
public @interface OptionalAddress {
    String message() default "Optional address cannot be empty when present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

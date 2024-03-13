package com.amalitech.gpuconfigurator.validation;

import com.amalitech.gpuconfigurator.annotation.OptionalAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalAddressValidator implements ConstraintValidator<OptionalAddress, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return !value.trim().isEmpty();
    }
}

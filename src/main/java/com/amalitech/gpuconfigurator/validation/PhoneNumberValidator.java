package com.amalitech.gpuconfigurator.validation;

import com.amalitech.gpuconfigurator.annotation.IsValidPhoneNumber;
import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<IsValidPhoneNumber, ContactRequest> {

    @Override
    public boolean isValid(ContactRequest contactRequest, ConstraintValidatorContext context) {
        if (contactRequest == null) {
            return true;
        }

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            PhoneNumber number = phoneNumberUtil.parse(contactRequest.getPhoneNumber(), contactRequest.getIso2Code().toUpperCase());

            return phoneNumberUtil.isValidNumberForRegion(number, contactRequest.getIso2Code().toUpperCase());
        } catch (NumberParseException e) {
            return false;
        }
    }
}

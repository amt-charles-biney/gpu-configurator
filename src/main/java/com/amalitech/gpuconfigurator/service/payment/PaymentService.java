package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.*;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

public interface PaymentService {

    InitializePaymentResponse initializePaymentTransaction(InitializePaymentRequest paymentRequest);
    VerifyPaymentResponse verifyPaymentTransaction(@Validated VerifyPaymentRequest reference, Principal user, UserSession userSession) throws NoSuchFieldException, JsonProcessingException;
    Payment savePaymentTransaction(PaymentObjectRequest paymentObjectRequest, Principal principal, UserSession userSession);
}

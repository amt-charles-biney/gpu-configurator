package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentObjectRequest;
import com.amalitech.gpuconfigurator.dto.Payment.VerifyPaymentRequest;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

public interface PaymentService {

    InitializePaymentResponse initializePaymentTransaction(InitializePaymentRequest paymentRequest);
    GenericResponse verifyPaymentTransaction(@Validated VerifyPaymentRequest reference, Principal user) throws NoSuchFieldException, JsonProcessingException;

    Payment savePaymentTransaction(PaymentObjectRequest paymentObjectRequest, Principal user);
}

package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.VerifyPaymentRequest;
import org.springframework.validation.annotation.Validated;

public interface PaymentService {

    public InitializePaymentResponse initializePaymentTransaction(InitializePaymentRequest paymentRequest);

    Object verifyPaymentTransaction(@Validated VerifyPaymentRequest reference);
    // public PaymentVerifiedResponse verifyPayment(PaymentVerifyRequest paymentResponse);
}

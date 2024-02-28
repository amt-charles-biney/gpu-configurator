package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.config.Paystack;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentVerifiedResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentVerifyRequest;

public interface PaymentService {

    public InitializePaymentResponse initializePaymentTransaction(InitializePaymentRequest paymentRequest);
    public PaymentVerifiedResponse verifyPayment(PaymentVerifyRequest paymentResponse);
}

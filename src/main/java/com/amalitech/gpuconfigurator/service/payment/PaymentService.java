package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.Payment.*;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;

public interface PaymentService {

    InitializePaymentResponse initializePaymentTransaction(InitializePaymentRequest paymentRequest);

    VerifyPaymentResponse verifyPaymentTransaction(@Validated VerifyPaymentRequest reference, Principal user, UserSession userSession) throws NoSuchFieldException, JsonProcessingException, APIConnectionException, APIException, AuthenticationException, InvalidRequestException;

    Payment savePaymentTransaction(PaymentObjectRequest paymentObjectRequest, Principal principal, UserSession userSession);
}

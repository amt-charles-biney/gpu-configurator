package com.amalitech.gpuconfigurator.controller.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.VerifyPaymentRequest;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.payment.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/v1/payment")
    public ResponseEntity<InitializePaymentResponse> initializePayment(@Validated @RequestBody InitializePaymentRequest paymentRequest) {
       return ResponseEntity.ok(paymentService.initializePaymentTransaction(paymentRequest));
    }

    @GetMapping("/v1/payment/{reference}")
    public ResponseEntity<Object> verifyPayment(@PathVariable String reference,
                                                @RequestAttribute("userSession") UserSession userSession,
                                                Principal principal) throws NoSuchFieldException, JsonProcessingException {
        VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest(reference);
        return ResponseEntity.ok(paymentService.verifyPaymentTransaction(verifyPaymentRequest, principal, userSession));
    }
}

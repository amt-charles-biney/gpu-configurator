package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.config.Paystack;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentVerifiedResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentVerifyRequest;
import com.amalitech.gpuconfigurator.exception.PaymentError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final static String API_URL  =  "https://api.paystack.co";
    private final static String API_KEY = "test_key";
    @Override
    public InitializePaymentResponse initializePaymentTransaction(@Validated InitializePaymentRequest paymentRequest) {
        
        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.set("Authorization", "Bearer " + API_KEY);
        paymentHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<InitializePaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, paymentHeaders);
        String paymentUrl = API_URL + "/transaction/initialize";

        ResponseEntity<InitializePaymentResponse> response = restTemplate.exchange(
                paymentUrl,
                HttpMethod.POST,
                requestEntity,
                InitializePaymentResponse.class
        );

        return response.getBody();
    }

    @Override
    public PaymentVerifiedResponse verifyPayment(PaymentVerifyRequest paymentResponse) {
        return null;
    }
}

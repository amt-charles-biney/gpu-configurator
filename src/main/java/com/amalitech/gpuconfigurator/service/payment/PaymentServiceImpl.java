package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.VerifyPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;

    @Value("${paystack.api.url")
    private String API_URL;
    @Value("${paystack.api.key")
    private String API_KEY;
    @Override
    public InitializePaymentResponse initializePaymentTransaction(@Validated InitializePaymentRequest paymentRequest) {

        InitializePaymentRequest paymentDetails = InitializePaymentRequest
                .builder()
                .amount(paymentRequest.amount() * 100)
                .currency(paymentRequest.currency())
                .email(paymentRequest.email())
                .reference(paymentRequest.currency())
                .build();
        
        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.set("Authorization", "Bearer " + API_KEY);
        paymentHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<InitializePaymentRequest> requestEntity = new HttpEntity<>(paymentDetails, paymentHeaders);
        String paymentUrl = API_URL + "/transaction/initialize";

        ResponseEntity<InitializePaymentResponse> response = restTemplate.exchange(
                paymentUrl,
                HttpMethod.POST,
                requestEntity,
                InitializePaymentResponse.class
        );

        return (InitializePaymentResponse) response.getBody();
    }


    @Override
    public Object verifyPaymentTransaction(@Validated VerifyPaymentRequest reference) {
        HttpHeaders verifyPaymentHeaders = new HttpHeaders();
        verifyPaymentHeaders.set("Authorization", "Bearer " + API_KEY);

        String verifyPaymentUrl = API_URL + "/transaction/verify/" + reference.reference();

        HttpEntity<?> requestEntity = new HttpEntity<>(verifyPaymentHeaders);

        return restTemplate.exchange(
                verifyPaymentUrl,
                HttpMethod.GET,
                requestEntity,
                Object.class
        ).getBody();

        // get the body
        // update the payment table
        // update the order table

    }
}

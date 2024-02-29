package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentRequest;
import com.amalitech.gpuconfigurator.dto.Payment.InitializePaymentResponse;
import com.amalitech.gpuconfigurator.dto.Payment.PaymentObjectRequest;
import com.amalitech.gpuconfigurator.dto.Payment.VerifyPaymentRequest;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.payment.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Value("${paystack.api.url}")
    private String API_URL;
    @Value("${paystack.api.key}")
    private String API_KEY;

    @Override
    public InitializePaymentResponse initializePaymentTransaction(@Validated InitializePaymentRequest paymentRequest) {

        InitializePaymentRequest paymentDetails = InitializePaymentRequest
                .builder()
                .amount(paymentRequest.amount() * 100)
                .currency(paymentRequest.currency())
                .email(paymentRequest.email())
                .reference(paymentRequest.reference())
                .channels(paymentRequest.channels())
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
    public GenericResponse verifyPaymentTransaction(@Validated VerifyPaymentRequest reference, Principal user) throws JsonProcessingException {

        HttpHeaders verifyPaymentHeaders = new HttpHeaders();
        verifyPaymentHeaders.set("Authorization", "Bearer " + API_KEY);

        String verifyPaymentUrl = API_URL + "/transaction/verify/" + reference.reference();

        HttpEntity<?> requestEntity = new HttpEntity<>(verifyPaymentHeaders);

        Object paymentResponse =  restTemplate.exchange(
                verifyPaymentUrl,
                HttpMethod.GET,
                requestEntity,
                Object.class
        ).getBody();

        PaymentObjectRequest paymentResponseJson = mapPaymentResponseToPayment(paymentResponse);

        if(paymentResponseJson.status()) {
            Payment paymentTransaction = savePaymentTransaction(paymentResponseJson, user);
        }

        // update the payment table
        // update the order table

        return new GenericResponse(200, "payment verified successful");
    }

    @Override
    public Payment savePaymentTransaction(PaymentObjectRequest paymentObjectRequest, Principal user) {

        Payment makePayment = Payment
                .builder()
                .ref(paymentObjectRequest.data().reference())
                .channel(paymentObjectRequest.data().channel())
                .currency(paymentObjectRequest.data().currency())
                .amount(paymentObjectRequest.data().amount())
                .build();

        return paymentRepository.save(makePayment);
    }

    public PaymentObjectRequest mapPaymentResponseToPayment(Object paymentResponse) throws IllegalArgumentException, JsonProcessingException {
        String paymentResponseJson = objectMapper.writeValueAsString(paymentResponse);
        return (PaymentObjectRequest) objectMapper.readValue(paymentResponseJson, PaymentObjectRequest.class);
    }

}
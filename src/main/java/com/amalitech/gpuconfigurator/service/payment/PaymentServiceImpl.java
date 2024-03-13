package com.amalitech.gpuconfigurator.service.payment;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.Payment.*;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.payment.PaymentRepository;
import com.amalitech.gpuconfigurator.service.order.OrderServiceImpl;
import com.easypost.exception.EasyPostException;
import com.easypost.exception.General.MissingParameterError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final OrderServiceImpl orderService;

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
    public VerifyPaymentResponse verifyPaymentTransaction(@Validated VerifyPaymentRequest reference, Principal user, UserSession userSession) throws JsonProcessingException, EasyPostException {

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
        UUID orderId = null;

        if(paymentResponseJson.status()) {
            Payment paymentTransaction = savePaymentTransaction(paymentResponseJson, user,  userSession);
            orderId = orderService.createOrder(paymentTransaction, user, userSession).orderId();

        }

        return VerifyPaymentResponse
                .builder()
                .orderId(orderId != null ? orderId.toString() : null)
                .message("payment verified successfully")
                .status(200)
                .build();
    }

    @Override
    public Payment savePaymentTransaction(PaymentObjectRequest paymentObjectRequest, Principal principal, UserSession userSession) {

        User user = null;
        UsernamePasswordAuthenticationToken authenticationToken =  ((UsernamePasswordAuthenticationToken) principal);

        if(authenticationToken != null) {
            user = (User) authenticationToken.getPrincipal();
        }

        Payment makePayment = Payment
                .builder()
                .ref(paymentObjectRequest.data().reference())
                .channel(paymentObjectRequest.data().channel())
                .currency(paymentObjectRequest.data().currency())
                .amount(paymentObjectRequest.data().amount())
                .user(user)
                .sessionId(userSession.getId().toString())
                .build();

        return paymentRepository.save(makePayment);
    }

    public PaymentObjectRequest mapPaymentResponseToPayment(Object paymentResponse) throws IllegalArgumentException, JsonProcessingException {
        String paymentResponseJson = objectMapper.writeValueAsString(paymentResponse);
        return (PaymentObjectRequest) objectMapper.readValue(paymentResponseJson, PaymentObjectRequest.class);
    }

}
package com.amalitech.gpuconfigurator.dto.PaymentInfo;

public record CardInfoRequest(String cardNumber, String expirationDate, String cardHolderName) {
}

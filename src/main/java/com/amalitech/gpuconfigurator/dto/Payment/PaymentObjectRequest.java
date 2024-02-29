package com.amalitech.gpuconfigurator.dto.Payment;

import com.amalitech.gpuconfigurator.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PaymentObjectRequest(
        boolean status,
        data data
) {

    public record data(
            String reference,
            BigDecimal amount,
            String channel,
            String currency
    ) {}
}

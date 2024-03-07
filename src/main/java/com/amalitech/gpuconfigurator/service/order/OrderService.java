package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import jakarta.transaction.Transactional;

import java.security.Principal;

public interface OrderService {
    @Transactional
    CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession);
}

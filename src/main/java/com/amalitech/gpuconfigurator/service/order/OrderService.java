package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.UserSession;

import java.security.Principal;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto request, Principal principal, UserSession userSession);
}

package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface OrderService {
    @Transactional
    CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession);

    Page<OrderResponseDto> getAllOrders(Integer page, Integer size, String sort);
}

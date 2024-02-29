package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;

import com.amalitech.gpuconfigurator.dto.GenericResponse;

import com.amalitech.gpuconfigurator.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public GenericResponse createOrder(CreateOrderRequestDto request, Principal principal, UserSession userSession) {

        return GenericResponse.builder()
                .message("Order successful")
                .status(200)
                .build();
    }
}

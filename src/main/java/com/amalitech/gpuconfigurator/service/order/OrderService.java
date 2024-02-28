package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto request);
}

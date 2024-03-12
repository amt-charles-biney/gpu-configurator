package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;

import java.util.List;

public interface OrderFiltering {
    List<OrderResponseDto> orders(String status);
}

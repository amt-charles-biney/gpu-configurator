package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderFiltering {
    List<OrderResponseDto> orders(String status, LocalDate startDate, LocalDate endDate);
}

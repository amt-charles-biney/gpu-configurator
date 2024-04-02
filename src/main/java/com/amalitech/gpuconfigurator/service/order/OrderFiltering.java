package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OrderFiltering {
    Page<OrderResponseDto> orders(String status, LocalDate startDate, LocalDate endDate, Integer page, Integer size);

    Page<OrderResponseDto> ordersUser(String status, LocalDate startDate, LocalDate endDate,Integer page, Integer size);
}

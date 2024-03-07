package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.order.OrderPageResponseDto;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @CrossOrigin
    @GetMapping("/v1/orders")

    public ResponseEntity<OrderPageResponseDto> getOrders(
    ) {

        return ResponseEntity.ok(orderService.getOrders(id));
    }

}

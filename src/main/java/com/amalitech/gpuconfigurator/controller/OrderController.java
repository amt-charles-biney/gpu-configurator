package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.order.OrderPageResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @CrossOrigin
    @GetMapping("/v1/admin/orders")

    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "100") Integer size

    ) {
     return ResponseEntity.ok(orderService.getAllOrders(page,size));
    }

}

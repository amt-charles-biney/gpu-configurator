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

    public ResponseEntity<OrderPageResponseDto> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {
        OrderPageResponseDto orderResponse = new OrderPageResponseDto();

        if(page != null && size != null){
            Page<OrderResponseDto> orders = orderService.getAllOrders(page,size,sort);
            orderResponse.setOrders(orders.getContent());
            orderResponse.setTotal(orders.getTotalElements());
        }
        return ResponseEntity.ok(orderResponse);
    }

}

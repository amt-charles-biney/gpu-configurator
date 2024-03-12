package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.OrderPageResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/v1/admin/orders")

    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size

    ) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @GetMapping("/v1/admin/orders/{id}")

    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    @CrossOrigin
    @DeleteMapping("/v1/admin/orders/all")
    public ResponseEntity<GenericResponse> deleteBulkOrders(@RequestBody List<String> ids) {
        GenericResponse deletedBulkProduct = orderService.deleteBulkProducts(ids);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkProduct);
    }

}

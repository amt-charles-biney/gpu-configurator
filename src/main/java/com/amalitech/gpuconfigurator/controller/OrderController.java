package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;

import com.amalitech.gpuconfigurator.service.order.OrderFiltering;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderFiltering orderFiltering;

    @GetMapping("/v1/admin/orders")
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Page<OrderResponseDto> resultPage;

        if (status != null || startDate != null || endDate != null) {
            List<OrderResponseDto> orderByStatus = orderFiltering.orders(status, startDate, endDate);
            resultPage = new PageImpl<>(orderByStatus, PageRequest.of(page, size), orderByStatus.size());
        } else {
            resultPage = orderService.getAllOrders(page, size);
        }

        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/v1/orders")
    public ResponseEntity<Page<OrderResponseDto>> getUserOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Principal principal
    ) {
        Page<OrderResponseDto> resultPage;

        if (status != null || startDate != null || endDate != null) {
            List<OrderResponseDto> orderByStatus = orderFiltering.orders(status, startDate, endDate);
            resultPage = new PageImpl<>(orderByStatus, PageRequest.of(page, size), orderByStatus.size());
        } else {
            resultPage = orderService.getAllUserOrders(page, size, principal);
        }

        return ResponseEntity.ok(resultPage);
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

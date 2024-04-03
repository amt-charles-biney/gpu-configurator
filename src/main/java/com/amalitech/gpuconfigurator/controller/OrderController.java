package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.CancelledDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;

import com.amalitech.gpuconfigurator.dto.order.OrderStatusUpdate;
import com.amalitech.gpuconfigurator.service.order.OrderFiltering;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import com.easypost.exception.EasyPostException;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderFiltering orderFiltering;

    @Operation(
            summary = "Get all orders (for admin)",
            method = "GET"
    )
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
            resultPage = orderFiltering.orders(status, startDate, endDate, page, size);
        } else {
            resultPage = orderService.getAllOrders(page, size);
        }

        return ResponseEntity.ok(resultPage);
    }

    @Operation(
            summary = "Get all user orders",
            method = "GET"
    )
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
            resultPage = orderFiltering.ordersUser(status, startDate, endDate, page, size);
        } else {
            resultPage = orderService.getAllUserOrders(page, size, principal);
        }

        return ResponseEntity.ok(resultPage);
    }

    @PatchMapping("/v1/orders/{id}")
    public GenericResponse cancelOrder(@PathVariable("id") UUID id,@RequestBody CancelledDto reason){

        orderService.cancelOrder(id, reason);
        return GenericResponse.builder().message("Order cancelled").status(200).build();
    }

    @Operation(
            summary = "Get order by ID",
            method = "GET"
    )
    @GetMapping("/v1/admin/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Update order status",
            method = "PATCH"
    )
    @PatchMapping("/v1/admin/orders/{id}")
    public ResponseEntity<GenericResponse> updateStatus(
            @PathVariable("id") UUID id,
            @RequestBody OrderStatusUpdate status
    ) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @Operation(
            summary = "Delete bulk orders",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/orders/all")
    public ResponseEntity<GenericResponse> deleteBulkOrders(@RequestBody List<String> ids) {
        GenericResponse deletedBulkProduct = orderService.deleteBulkProducts(ids);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkProduct);
    }

}

package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.service.order.OrderService;
import com.easypost.exception.EasyPostException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class shipmentController {

    private final OrderService orderService;

    @Operation(
            summary = "Create shipment",
            method = "POST"
    )
    @PostMapping("/v1/admin/shipment")
    public GenericResponse createShipment(@RequestBody UUID orderId) throws EasyPostException {
        orderService.shipment(orderId);
        return GenericResponse.builder().message("Shipment created").status(201).build();
    }
}

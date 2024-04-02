package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.service.order.OrderService;
import com.easypost.exception.EasyPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class shipmentController {

    private final OrderService orderService;
    @GetMapping("/v1/admin/shipment/{orderId}")
    public void createShipment(@PathVariable("orderId") UUID orderId) throws EasyPostException {
        orderService.shipment(orderId);
    }
}

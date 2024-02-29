package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.shipping.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @PostMapping("/v1/shipping")
    public ResponseEntity<ShippingResponse> create(
            @Valid ShippingRequest dto,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        ShippingResponse response = shippingService.create(dto, principal, userSession);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/v1/shipping")
    public ResponseEntity<Page<ShippingResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(shippingService.findAll(page, size));
    }

    @GetMapping("/v1/shipping/{shippingId}")
    public ResponseEntity<ShippingResponse> findById(@PathVariable UUID shippingId) {
        return ResponseEntity.ok(shippingService.findById(shippingId));
    }

    @PutMapping("/v1/shipping/{shippingId}")
    public ResponseEntity<ShippingResponse> update(@PathVariable UUID shippingId, @Valid ShippingRequest dto) {
        return ResponseEntity.ok(shippingService.update(shippingId, dto));
    }

    @DeleteMapping("/v1/shipping/{shippingId}")
    public ResponseEntity<GenericResponse> delete(@PathVariable UUID shippingId) {
        return ResponseEntity.ok(shippingService.delete(shippingId));
    }
}

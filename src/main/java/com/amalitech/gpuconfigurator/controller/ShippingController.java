package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.shipping.ShippingService;
import com.easypost.exception.EasyPostException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @PostMapping("/v1/shipping")
    public ResponseEntity<ShippingResponse> create(
            @RequestBody @Valid ShippingRequest dto,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) throws EasyPostException {
        User user = principal == null
                ? null
                : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        ShippingResponse response = shippingService.create(dto, user, userSession);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

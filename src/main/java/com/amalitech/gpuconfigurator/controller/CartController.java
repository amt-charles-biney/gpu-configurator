package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @CrossOrigin
    @GetMapping("/v1/carts/item-count")
    public ResponseEntity<CartItemsCountResponse> getCartItemsCount(
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.getCartItemsCount(principal));
    }
}

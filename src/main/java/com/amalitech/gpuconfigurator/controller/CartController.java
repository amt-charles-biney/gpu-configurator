package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.service.cart.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @CrossOrigin
    @GetMapping("/v1/carts/item-count")
    public ResponseEntity<CartItemsCountResponse> getCartItemsCount(Principal principal, HttpSession session) {
        return ResponseEntity.ok(cartService.getCartItemsCount(principal, session));
    }

    @CrossOrigin
    @PostMapping("/v1/carts/add-item/{productId}")
    public ResponseEntity<AddCartItemResponse> addCartItem(
            @PathVariable UUID productId,
            @RequestParam(required = false) Boolean warranty,
            @RequestParam(required = false) String components,
            Principal principal,
            HttpSession session
    ) {
        return ResponseEntity.ok(cartService.addCartItem(productId, warranty, components, principal, session));
    }

    @CrossOrigin
    @DeleteMapping("/v1/carts/delete-item/{configuredProductId}")
    public ResponseEntity<DeleteCartItemResponse> deleteCartItem(@PathVariable UUID configuredProductId, Principal principal, HttpSession session) {
        return ResponseEntity.ok(cartService.deleteCartItem(configuredProductId, principal, session));
    }
}

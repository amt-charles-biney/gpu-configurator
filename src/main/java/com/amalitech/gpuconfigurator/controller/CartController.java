package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.cart.CartService;
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
    public ResponseEntity<CartItemsCountResponse> getCartItemsCount(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.getCartItemsCount(principal, userSession));
    }

    @CrossOrigin
    @PostMapping("/v1/carts/add-item/{productId}")
    public ResponseEntity<AddCartItemResponse> addCartItem(
            @PathVariable UUID productId,
            @RequestParam(required = false) Boolean warranty,
            @RequestParam(required = false) String components,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.addCartItem(productId, warranty, components, principal, userSession));
    }

    @CrossOrigin
    @DeleteMapping("/v1/carts/delete-item/{configuredProductId}")
    public ResponseEntity<DeleteCartItemResponse> deleteCartItem(
            @PathVariable UUID configuredProductId,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.deleteCartItem(configuredProductId, principal, userSession));
    }

    @CrossOrigin
    @GetMapping("/v1/carts")
    public ResponseEntity<CartItemsResponse> getCartItems(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.getCartItems(principal, userSession));
    }
}

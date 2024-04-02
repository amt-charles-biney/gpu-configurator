package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cart.*;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(
            summary = "Get the count of items in the cart",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/carts/item-count")
    public ResponseEntity<CartItemsCountResponse> getCartItemsCount(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.getCartItemsCount(principal, userSession));
    }

    @Operation(
            summary = "Add an item to the cart",
            method = "POST"
    )
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

    @Operation(
            summary = "Delete an item from the cart",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/carts/delete-item/{configuredProductId}")
    public ResponseEntity<DeleteCartItemResponse> deleteCartItem(
            @PathVariable UUID configuredProductId,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.deleteCartItem(configuredProductId, principal, userSession));
    }

    @Operation(
            summary = "Get items from the cart",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/carts")
    public ResponseEntity<CartItemsResponse> getCartItems(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        return ResponseEntity.ok(cartService.getCartItems(principal, userSession));
    }


    @Operation(
            summary = "Update the quantity of an item in the cart",
            method = "POST"
    )
    @PostMapping("/v1/carts/update-quantity")
    public ResponseEntity<UpdateCartItemQuantityResponse> updateCartItemQuantity(
            @RequestBody @Valid UpdateCartItemQuantityRequest dto,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        User user = principal == null
                ? null
                : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(cartService.updateCartItemQuantity(dto, user, userSession));
    }
}

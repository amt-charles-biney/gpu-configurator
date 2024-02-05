package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.UUID;

public interface CartService {
    CartItemsCountResponse getCartItemsCount(Principal principal, HttpSession session);

    AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, HttpSession session);
}

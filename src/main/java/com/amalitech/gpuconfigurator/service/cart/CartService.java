package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;

import java.security.Principal;

public interface CartService {
    CartItemsCountResponse getCartItemsCount(Principal principal);
}

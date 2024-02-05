package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;

public interface CartService {
    CartItemsCountResponse getCartItemsCount(Principal principal, HttpSession session);
}

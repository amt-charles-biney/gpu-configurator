package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.model.UserSession;

import java.security.Principal;
import java.util.UUID;

public interface CartService {
    CartItemsCountResponse getCartItemsCount(Principal principal, UserSession userSession);

    AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, UserSession userSession);

    DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, UserSession userSession);

    CartItemsResponse getCartItems(Principal principal, UserSession userSession);
}

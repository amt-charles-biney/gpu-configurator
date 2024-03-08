package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.*;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;

import java.security.Principal;
import java.util.UUID;

public interface CartService {
    CartItemsCountResponse getCartItemsCount(Principal principal, UserSession userSession);

    AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, UserSession userSession);

    DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, UserSession userSession);

    CartItemsResponse getCartItems(Principal principal, UserSession userSession);

    UpdateCartItemQuantityResponse updateCartItemQuantity(UpdateCartItemQuantityRequest dto, User user, UserSession userSession);
}

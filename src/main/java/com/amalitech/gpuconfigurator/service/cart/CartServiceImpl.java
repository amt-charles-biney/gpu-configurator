package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ConfigurationRepository configuredProductRepository;

    @Override
    public CartItemsCountResponse getCartItemsCount(Principal principal, HttpSession session) {
        Cart cart;
        if (principal == null) {
            cart = (Cart) session.getAttribute("cart");
        } else {
            var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            cart = user.getCart();
        }

        long cartItemsCount = 0;
        if (cart != null) {
            cartItemsCount = configuredProductRepository.countByCartId(cart.getId());
        }

        return CartItemsCountResponse.builder()
                .count(cartItemsCount)
                .build();
    }
}

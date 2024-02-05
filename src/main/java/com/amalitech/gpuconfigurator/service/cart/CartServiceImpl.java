package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.CartRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ConfigurationRepository configuredProductRepository;

    @Override
    public CartItemsCountResponse getCartItemsCount(Principal principal) {
        if (principal == null) {
            return null;
        }

        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        long cartItemsCount = 0;
        if (user.getCart() != null) {
            cartItemsCount = configuredProductRepository.countByCartId(user.getCart().getId());
        }

        return CartItemsCountResponse.builder()
                .count(cartItemsCount)
                .build();
    }
}

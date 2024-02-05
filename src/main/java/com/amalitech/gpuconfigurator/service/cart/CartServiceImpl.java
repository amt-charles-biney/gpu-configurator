package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.CartRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ConfigurationRepository configuredProductRepository;
    private final ConfigurationService configuredProductService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItemsCountResponse getCartItemsCount(Principal principal, HttpSession session) {
        var optionalCart = this.getUserOrGuestCart(this.getUser(principal), session);

        long cartItemsCount = 0;
        if (optionalCart.isPresent()) {
            cartItemsCount = configuredProductRepository.countByCartId(optionalCart.get().getId());
        }

        return CartItemsCountResponse.builder()
                .count(cartItemsCount)
                .build();
    }

    @Override
    public AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, HttpSession session) {
        var configuredProductResponse = configuredProductService.saveConfiguration(productId.toString(), warranty, components);

        var user = this.getUser(principal);
        Cart cart = getUserOrGuestCart(user, session).orElseGet(Cart::new);

        var optionalConfiguredProduct = configuredProductRepository.findById(UUID.fromString(configuredProductResponse.Id()));
        if (optionalConfiguredProduct.isPresent()) {
            var configuredProduct = optionalConfiguredProduct.get();
            configuredProduct.setCart(cart);
            configuredProductRepository.save(configuredProduct);
        }
        cartRepository.save(cart);

        if (user == null) {
            session.setAttribute("cart", cart);
        } else if (user.getCart() == null) {
            user.setCart(cart);
            userRepository.save(user);
        }

        return AddCartItemResponse.builder()
                .message("Configured product added to cart successfully")
                .configuration(configuredProductResponse)
                .build();
    }

    private Optional<Cart> getUserOrGuestCart(User user, HttpSession session) {
        if (user == null) {
            return Optional.ofNullable((Cart) session.getAttribute("cart"));
        }
        return Optional.ofNullable(user.getCart());
    }

    private User getUser(Principal principal) {
        if (principal != null) {
            return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        return null;
    }
}

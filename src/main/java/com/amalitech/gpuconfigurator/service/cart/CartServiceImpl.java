package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CartRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
        User user = this.getUser(principal);
        var optionalCart = this.getUserOrGuestCart(user, session);

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
        var user = this.getUser(principal);
        Cart cart = this.getUserOrGuestCart(user, session).orElseGet(Cart::new);

        if (cart.getId() == null) {
            cart = cartRepository.save(cart);
        }

        if (user == null) {
            session.setAttribute("cart_id", cart.getId());
        } else if (user.getCart() == null) {
            user.setCart(cart);
            userRepository.save(user);
        }

        var configuredProductResponse = configuredProductService.saveConfiguration(productId.toString(), warranty, components, cart);

        return AddCartItemResponse.builder()
                .message("Configured product added to cart successfully")
                .configuration(configuredProductResponse)
                .build();
    }

    @Override
    public DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, HttpSession session) {
        var optionalCart = this.getUserOrGuestCart(this.getUser(principal), session);

        if (optionalCart.isEmpty()) {
            return DeleteCartItemResponse.builder().message("User has no items in cart").build();
        }

        var optionalConfiguredProduct = configuredProductRepository.findByIdAndCartId(configuredProductId, optionalCart.get().getId());
        optionalConfiguredProduct.ifPresent(configuredProductRepository::delete);

        return DeleteCartItemResponse.builder().message("Configured product deleted successfully").build();
    }

    @Override
    public CartItemsResponse getCartItems(Principal principal, HttpSession session) {
        var optionalCart = this.getUserOrGuestCart(this.getUser(principal), session);

        if (optionalCart.isEmpty()) {
            return new CartItemsResponse(new ArrayList<>(), 0);
        }

        List<ConfigurationResponseDto> configuredProducts = configuredProductRepository.findByCartId(optionalCart.get().getId())
                .stream()
                .map(this::mapToConfigurationResponseDto)
                .toList();

        return new CartItemsResponse(configuredProducts, configuredProducts.size());
    }

    private Optional<Cart> getUserOrGuestCart(User user, HttpSession session) {
        if (user == null) {
            return Optional.ofNullable(this.getGuestCart(session));
        }
        return Optional.ofNullable(this.getUserCart(user));
    }

    private Cart getGuestCart(HttpSession session) {
        UUID cartId = (UUID) session.getAttribute("cart_id");
        if (cartId == null) {
            return null;
        }
        return cartRepository.findById(cartId).orElse(null);
    }

    private Cart getUserCart(User user) {
        return user.getCart();
    }

    private User getUser(Principal principal) {
        if (principal != null) {
            return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        return null;
    }

    private ConfigurationResponseDto mapToConfigurationResponseDto(Configuration configuredProduct) {
        var product = configuredProduct.getProduct();

        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuredProduct.getId()))
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .productDescription(product.getProductDescription())
                .productCoverImage(product.getCoverImage())
                .totalPrice(configuredProduct.getTotalPrice())
                .warranty(null)
                .vat(null)
                .configuredPrice(null)
                .configured(configuredProduct.getConfiguredOptions())
                .build();
    }
}

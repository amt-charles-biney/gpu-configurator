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
        Cart cart = this.getUserOrGuestCart(user, session).orElseGet(Cart::new);

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

    @Override
    public DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, HttpSession session) {
        var optionalCart = getUserOrGuestCart(this.getUser(principal), session);

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
            return new CartItemsResponse(null, 0);
        }

        List<ConfigurationResponseDto> configuredProducts = configuredProductRepository.findByCartId(optionalCart.get().getId())
                .stream()
                .map(this::mapToConfigurationResponseDto)
                .toList();

        return new CartItemsResponse(configuredProducts, configuredProducts.size());
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

    private ConfigurationResponseDto mapToConfigurationResponseDto(Configuration configuredProduct) {
        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuredProduct.getId()))
                .productName(configuredProduct.getProduct().getProductName())
                .productId(configuredProduct.getProduct().getProductId())
                .totalPrice(configuredProduct.getTotalPrice())
                .warranty(null)
                .vat(null)
                .configuredPrice(null)
                .productPrice(BigDecimal.valueOf(configuredProduct.getProduct().getProductPrice()))
                .configured(configuredProduct.getConfiguredOptions())
                .build();
    }
}

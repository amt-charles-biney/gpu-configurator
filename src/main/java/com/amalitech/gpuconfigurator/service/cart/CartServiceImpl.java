package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.AddCartItemResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.dto.cart.CartItemsResponse;
import com.amalitech.gpuconfigurator.dto.cart.DeleteCartItemResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.exception.CannotAddItemToCartException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.*;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ConfigurationRepository configuredProductRepository;
    private final ConfigurationService configuredProductService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserSessionRepository userSessionRepository;
    private final CompatibleOptionRepository compatibleOptionRepository;

    @Override
    public CartItemsCountResponse getCartItemsCount(Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = getUserOrGuestCart(user, userSession);

        long cartItemsCount = optionalCart
                .map(cart -> configuredProductRepository.countByCartId(cart.getId()))
                .orElse(0L);

        return CartItemsCountResponse.builder()
                .count(cartItemsCount)
                .build();
    }

    @Override
    public AddCartItemResponse addCartItem(UUID productId, Boolean warranty, String components, Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> cartOptional = getUserOrGuestCart(user, userSession);
        Cart cart = cartOptional.orElseGet(() -> cartRepository.save(new Cart()));

        if (user == null) {
            userSession.setCart(cart);
            userSessionRepository.save(userSession);
        } else if (user.getCart() == null) {
            user.setCart(cart);
            userRepository.save(user);
        }

        long cartItemsCount = configuredProductRepository.countByCartId(cart.getId());

        if (cartItemsCount != 0) {
            throw new CannotAddItemToCartException("Cart already contains one configured product. Checkout configured product to continue.");
        }

        ConfigurationResponseDto configuredProductResponse = configuredProductService.saveConfiguration(productId.toString(), warranty, components, cart);

        return AddCartItemResponse.builder()
                .message("Configured product added to cart successfully")
                .configuration(setMaximumStock(configuredProductResponse))
                .build();
    }

    @Override
    public DeleteCartItemResponse deleteCartItem(UUID configuredProductId, Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = getUserOrGuestCart(user, userSession);

        if (optionalCart.isEmpty()) {
            return DeleteCartItemResponse.builder().message("Configured product deleted successfully").build();
        }

        configuredProductRepository
                .findByIdAndCartId(configuredProductId, optionalCart.get().getId())
                .ifPresent(configuredProductRepository::delete);

        return DeleteCartItemResponse.builder().message("Configured product deleted successfully").build();
    }

    @Override
    public CartItemsResponse getCartItems(Principal principal, UserSession userSession) {
        User user = getUser(principal);
        Optional<Cart> optionalCart = this.getUserOrGuestCart(user, userSession);

        if (optionalCart.isEmpty()) {
            return new CartItemsResponse(new ArrayList<>(), 0);
        }

        List<ConfigurationResponseDto> configuredProducts = configuredProductRepository.findByCartId(optionalCart.get().getId())
                .stream()
                .map(this::mapToConfigurationResponseDto)
                .map(this::setMaximumStock)
                .toList();

        return new CartItemsResponse(configuredProducts, configuredProducts.size());
    }

    private Optional<Cart> getUserOrGuestCart(User user, UserSession userSession) {
        if (user == null) {
            return Optional.ofNullable(userSession.getCart());
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
        Product product = configuredProduct.getProduct();

        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuredProduct.getId()))
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getTotalProductPrice())
                .productDescription(product.getProductDescription())
                .productCoverImage(product.getProductCase().getCoverImageUrl())
                .totalPrice(configuredProduct.getTotalPrice())
                .warranty(null)
                .vat(null)
                .configuredPrice(null)
                .configured(configuredProduct.getConfiguredOptions())
                .build();
    }

    private ConfigurationResponseDto setMaximumStock(ConfigurationResponseDto dto) {
        Set<UUID> compatibleOptionIds = dto.configured()
                .stream()
                .map(ConfigOptions::getOptionId)
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        List<CompatibleOption> compatibleOptions = compatibleOptionRepository.findAllById(compatibleOptionIds);

        int stock = Integer.MAX_VALUE;
        for (CompatibleOption compatibleOption : compatibleOptions) {
            stock = Math.min(stock, compatibleOption.getAttributeOption().getInStock());
        }

        return ConfigurationResponseDto.builder()
                .Id(dto.Id())
                .totalPrice(dto.totalPrice())
                .productId(dto.productId())
                .productName(dto.productName())
                .productPrice(dto.productPrice())
                .productDescription(dto.productDescription())
                .productCoverImage(dto.productCoverImage())
                .configuredPrice(dto.configuredPrice())
                .configured(dto.configured())
                .vat(dto.vat())
                .warranty(dto.warranty())
                .stock(compatibleOptions.isEmpty() ? 0 : stock)
                .build();
    }
}

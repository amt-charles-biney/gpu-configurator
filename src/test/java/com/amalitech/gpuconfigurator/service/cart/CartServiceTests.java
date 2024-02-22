package com.amalitech.gpuconfigurator.service.cart;

import com.amalitech.gpuconfigurator.dto.cart.CartItemsCountResponse;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.CartRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {
    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ConfigurationRepository configuredProductRepository;

    @Mock
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Mock
    private HttpSession session;

    private Cart cart;

    private User user;

    @BeforeEach
    public void init() {
        cart = new Cart();
        cart.setId(UUID.randomUUID());

        user = new User();
    }

    @Test
    public void getCartItemsCount_whenUserCartIsEmpty_returnsZeroCartItemsCountResponse() {
        CartItemsCountResponse result = cartService.getCartItemsCount(authenticationToken, session);

        Assertions.assertThat(result.getCount()).isEqualTo(0);
    }

    @Test
    public void getCartItemsCount_whenUserCartNotEmpty_returnsCartItemsCountResponse() {
        long count = 1;
        user.setCart(cart);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(configuredProductRepository.countByCartId(any(UUID.class))).thenReturn(count);

        CartItemsCountResponse result = cartService.getCartItemsCount(authenticationToken, session);

        Assertions.assertThat(result.getCount()).isEqualTo(count);
    }

    @Test
    public void getCartItemsCount_whenGuestCartIsEmpty_returnsZeroCartItemsCountResponse() {
        when(session.getAttribute("cart_id")).thenReturn(null);

        CartItemsCountResponse result = cartService.getCartItemsCount(null, session);

        Assertions.assertThat(0).isEqualTo(result.getCount());
    }

    @Test
    public void getCartItemsCount_whenGuestCartNotEmpty_returnsCartItemsCountResponse() {
        long count = 1;
        when(session.getAttribute("cart_id")).thenReturn(cart.getId());
        when(cartRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(cart));
        when(configuredProductRepository.countByCartId(any(UUID.class))).thenReturn(count);

        CartItemsCountResponse result = cartService.getCartItemsCount(null, session);

        Assertions.assertThat(result.getCount()).isEqualTo(count);
    }
}

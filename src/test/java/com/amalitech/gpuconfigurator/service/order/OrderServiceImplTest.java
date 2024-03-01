package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.model.Cart;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Mock
    private UserRepository userRepository;

    @Test
    void createOrder() {

        // Given
        User user = User.builder()
                .role(Role.USER)
                .password("Some-test")
                .isVerified(true)
                .email("admin@emial.com")
                .isVerified(true)
                .cart(new Cart())
                .firstName("john")
                .lastName("doe")
                .build();


        UserSession userSession = new UserSession();

        // Mocking authenticationToken


        Payment payment = Payment.builder()
                .user(user)
                .currency("GHS")
                .build();

        // When
        when(userRepository.save(user)).thenReturn(user);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        CreateOrderDto response = orderService.createOrder(payment, authenticationToken, userSession);

        // Then
        assertNotNull(response);
        // Verify that the save method is called with the correct argument
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
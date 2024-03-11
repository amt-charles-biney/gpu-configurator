package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.awaitility.Awaitility.given;
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

    private User user;

    private UserSession userSession;

    private Payment payment;

    private Order order;

    @BeforeEach
    void setup() {
        // Given
        user = User.builder()
                .role(Role.USER)
                .password("Some-test")
                .isVerified(true)
                .email("admin@emial.com")
                .isVerified(true)
                .cart(new Cart())
                .firstName("john")
                .lastName("doe")
                .build();


        userSession = new UserSession();

        // Mocking authenticationToken


        payment = Payment.builder()
                .user(user)
                .currency("GHS")
                .build();

        order = Order.builder()
                .id(UUID.randomUUID())
                .cart(new Cart())
                .status(OrderType.PENDING)
                .payment(payment)
                .user(user)
                .build();
    }

    @Test
    void createOrder() {

        //given

        // When
        when(userRepository.save(user)).thenReturn(user);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        CreateOrderDto response = orderService.createOrder(payment, authenticationToken, userSession);

        // Then
        assertNotNull(response);
        // Verify that the save method is called with the correct argument
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void deleteOrder() {

        //given

        // When
        GenericResponse response = orderService.deleteOrder(order.getId());
        // Then
        assertNotNull(response);
        assertEquals(200, response.status());
        assertEquals("Order id " + order.getId() + " deleted", response.message());
    }
}
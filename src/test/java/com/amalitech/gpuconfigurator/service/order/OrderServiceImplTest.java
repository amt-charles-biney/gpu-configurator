package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.easypost.exception.EasyPostException;
import com.easypost.model.Tracker;
import com.easypost.service.EasyPostClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.UUID;

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

    @Mock
    private EasyPostClient easyPostClientMock;

    private User user;

    private UserSession userSession;

    private Payment payment;

    private Order order;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
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
                .status("Pending")
                .payment(payment)
                .user(user)
                .build();
    }

    @Test
    void createOrder() throws EasyPostException {

        //given
//        Tracker trackerMock = mock(Tracker.class);
//        when(easyPostClientMock.tracker.retrieve(anyString())).thenReturn(trackerMock);
        // When
        when(userRepository.save(user)).thenReturn(user);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        CreateOrderDto response = orderService.createOrder(payment, authenticationToken, userSession);

        // Then
        assertNotNull(response);
        // Verify that the save method is called with the correct argument
        verify(orderRepository, times(1)).save(any(Order.class));
    }

//    @Test
//    void deleteOrder() {
//
//        //given
//
//        // When
//        GenericResponse response = orderService.deleteOrder(order.getId());
//        // Then
//        assertNotNull(response);
//        assertEquals(200, response.status());
//        assertEquals("Order id " + order.getId() + " deleted", response.message());
//    }
}
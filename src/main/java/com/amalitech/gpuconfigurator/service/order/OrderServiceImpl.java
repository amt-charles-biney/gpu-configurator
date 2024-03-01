package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.dto.GenericResponse;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.repository.OrderRepository;


import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    @Override
    @Transactional
    public CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession) {

        User user = null;
        UsernamePasswordAuthenticationToken authenticationToken = ((UsernamePasswordAuthenticationToken) principal);

        if (authenticationToken != null) {
            user = (User) authenticationToken.getPrincipal();
        }

        Order.OrderBuilder orderBuilder = Order.builder();

        if (user != null) {
            orderBuilder.cart(user.getCart());
            user.setCart(null);
            userRepository.save(user);

        } else {
            orderBuilder.cart(userSession.getCart());
            userSession.setCart(null);
            userSessionRepository.save(userSession);

        }

        Order order = orderBuilder
                .status(OrderType.PENDING)
                .user(user)
                .payment(payment).build();
        orderRepository.save(order);

        return CreateOrderDto.builder()
                .orderId(order.getId())
                .build();
    }
}

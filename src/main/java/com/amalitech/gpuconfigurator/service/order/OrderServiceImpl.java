package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;

import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


import java.security.Principal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    @Transactional
    public GenericResponse createOrder(Payment payment, Principal principal, UserSession userSession) {

        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        Order.OrderBuilder orderBuilder = Order.builder();
        if (user != null) {
            orderBuilder.cart(user.getCart());
            user.setCart(new Cart());
            userRepository.save(user);

        } else {
            orderBuilder.cart(userSession.getCart());
            userSession.setCart(new Cart());
            userSessionRepository.save(userSession);

        }

        Order order = orderBuilder.status(OrderType.PENDING).payment(payment).build();
        orderRepository.save(order);

        return GenericResponse.builder()
                .status(200)
                .message("Order successful")
                .build();
    }
}

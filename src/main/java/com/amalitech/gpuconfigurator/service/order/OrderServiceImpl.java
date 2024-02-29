package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request, Principal principal, UserSession userSession) {

        User customer = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new NotFoundException("No user found");
        });

        var cartItems = cartService.getCartItems(principal,userSession);

        Iterable<ConfigurationResponseDto> configuredProducts = cartItems.configuredProducts();


        //todo : get payments infor from here, this code below will change
        Payment payment = Payment.builder()
                .amount(BigDecimal.ZERO)
                .ref("")
                .channel("mobile_money")
                .currency("GHS")
                .user(customer)
                .build();

        // to get order product here
        OrderProduct orderProduct = OrderProduct.builder()
                .coverImage("")
                .productName("")
                .totalPrice(BigDecimal.ZERO)
                .build();


        Order order = Order.builder()
                .user(customer)
                .orderProduct(orderProduct)
                .payments(payment)
                .status(OrderType.PENDING)
                .build();

        orderRepository.save(order);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .status(order.getStatus())
                .productName(orderProduct.getProductName())
                .paymentMethod(order.getPayments().getChannel())
                .productImageCover(orderProduct.getCoverImage())
                .totalPrice(orderProduct.getTotalPrice())
                .date(order.getCreatedAt())
                .build();

    }
}

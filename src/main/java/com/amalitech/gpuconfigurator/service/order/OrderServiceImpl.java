package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.OrderProduct;
import com.amalitech.gpuconfigurator.model.OrderType;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {

//        User customer = userRepository.findById(request.getUserId()).orElseThrow(() -> {
//            throw new NotFoundException("No user found");
//        });

        User customer = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            try {
                throw new Exception("No user found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

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

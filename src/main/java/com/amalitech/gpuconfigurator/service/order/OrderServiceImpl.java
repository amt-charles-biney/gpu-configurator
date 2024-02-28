package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.OrderType;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {

        User customer = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new NotFoundException("No user found");
        });

        //todo : get payments infor from here


        Order order = Order.builder()
                .user(customer)
                .payments(//todo : add payment here)
                .status(OrderType.PENDING)
                .build();

         orderRepository.save(order);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .customerName(order.getUser().getFirstName() + " "+ order.getUser().getLastName())
                .status(order.getStatus())
                .productName(// todo : product here)
                .paymentMethod(order.getPayments().//todo: get channel here)
                .productImageCover(//todo: product image here)
                .totalPrice(// todo: product total price here)
                .date(order.getCreatedAt())
                .build();
    }
}

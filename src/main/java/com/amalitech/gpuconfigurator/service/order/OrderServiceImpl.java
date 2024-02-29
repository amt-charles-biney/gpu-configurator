package com.amalitech.gpuconfigurator.service.order;



import com.amalitech.gpuconfigurator.dto.GenericResponse;

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
    public GenericResponse createOrder(Payment payment, Principal principal, UserSession userSession) {

        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

//        var cartItems = cartService.getCartItems(principal, userSession);

//        Iterable<ConfigurationResponseDto> configuredProducts = cartItems.configuredProducts();

//        List<ConfiguredProduct> configuredProductList = StreamSupport.stream(configuredProducts.spliterator(), false)
//                .map(configDto -> ConfiguredProduct.builder()
//                        .configurationName(configDto.productName())
//                        .configurationPrice(configDto.configuredPrice())
//                        .price(configDto.productPrice())
//                        .coverImage(configDto.productCoverImage())
//                        .configurationDescription(configDto.productDescription())
//                        .configs(configDto.configured().stream()
//                                .map(options -> ConfiguredProduct.Configs.builder()
//                                        .optionName(options.getOptionName())
//                                        .optionPrice(options.getOptionPrice())
//                                        .OptionType(options.getOptionType())
//                                        .build())
//                                .collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());

//        List<ConfiguredProduct> configuredProductList = StreamSupport.stream(configuredProducts.spliterator(), false)
//                .map(configDto -> ConfiguredProduct.builder()
//                        .configurationName(configDto.productName())
//                        .configurationPrice(configDto.configuredPrice())
//                        .price(configDto.productPrice())
//                        .coverImage(configDto.productCoverImage())
//                        .configurationDescription(configDto.productDescription())
//                        .build())
//                .collect(Collectors.toList());

//
//        BigDecimal totalPrice = StreamSupport.stream(configuredProducts.spliterator(), false)
//                .map(ConfigurationResponseDto::totalPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);


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


//                .configuredProducts(configuredProductList)
//                .user(user != null ? user : null)
//                .payment(payment)
//                .totalPrice(totalPrice)
//                .status(OrderType.PENDING);

        orderRepository.save(order);

        return GenericResponse.builder()
                .message("Order successful")
                .status(200)
                .build();
    }
}

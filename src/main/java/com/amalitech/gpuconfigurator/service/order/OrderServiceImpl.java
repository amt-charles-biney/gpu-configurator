package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.constant.ShipmentContants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.OrderRepository;


import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.easypost.exception.EasyPostException;
import com.easypost.model.Shipment;
import com.easypost.service.EasyPostClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    @Value("${easy-test-key}")
    private String easyPost;

    @Override
    @Transactional
    public CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession) throws EasyPostException {

        EasyPostClient client = new EasyPostClient(easyPost);


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


        Map<String, Object> fromAddressMap = new HashMap<String, Object>();
        fromAddressMap.put("company", "EasyPost");
        fromAddressMap.put("street1", "417 MONTGOMERY ST");
        fromAddressMap.put("street2", "FLOOR 5");
        fromAddressMap.put("city", "SAN FRANCISCO");
        fromAddressMap.put("state", "CA");
        fromAddressMap.put("country", "US");
        fromAddressMap.put("zip", "94104");
        fromAddressMap.put("phone", "415-123-4567");

        Map<String, Object> toAddressMap = new HashMap<String, Object>();
        toAddressMap.put("name", "Dr. Steve Brule");
        toAddressMap.put("street1", "179 N Harbor Dr");
        toAddressMap.put("city", "Redondo Beach");
        toAddressMap.put("state", "CA");
        toAddressMap.put("country", "US");
        toAddressMap.put("zip", "90277");
        toAddressMap.put("phone", "310-808-5243");

        Map<String, Object> parcelMap = new HashMap<String, Object>();
        parcelMap.put("weight", 22.9);
        parcelMap.put("height", 12.1);
        parcelMap.put("width", 8);
        parcelMap.put("length", 19.8);

        Map<String, Object> shipmentMap = new HashMap<String, Object>();
        shipmentMap.put("from_address", fromAddressMap);
        shipmentMap.put("to_address", toAddressMap);
        shipmentMap.put("parcel", parcelMap);


        Shipment shipment = client.shipment.create(shipmentMap);

        Shipment boughtShipment = client.shipment.buy(shipment.getId(), shipment.lowestRate());


        Order order = orderBuilder
                .tracking_id((boughtShipment.getTrackingCode()))
                .status(boughtShipment.getStatus())
                .user(user)
                .payment(payment).build();
        orderRepository.save(order);

        return CreateOrderDto.builder()
                .orderId(order.getId())
                .build();
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return orderRepository.findAll(pageable).map(this::mapOrderToOrderResponseDto);
    }


    @Override
    public OrderResponseDto getOrderById(UUID id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));

        return mapOrderToOrderResponseDto(order);
    }

    @Override
    public GenericResponse deleteBulkProducts(List<String> ids) {
        List<UUID> selectedProductUUID = ids.stream()
                .map(UUID::fromString)
                .toList();

        orderRepository.deleteAllById(selectedProductUUID);
        return new GenericResponse(HttpStatus.ACCEPTED.value(), "deleted bulk products successful");
    }

    private OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderId(order.getTracking_id())
                .configuredProduct(order.getCart().getConfiguredProducts())
                .productCoverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .productName(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductName()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .status(order.getStatus())
                .customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .totalPrice(order.getCart().getConfiguredProducts().stream()
                        .map(Configuration::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .date(order.getCreatedAt())
                .build();
    }


}

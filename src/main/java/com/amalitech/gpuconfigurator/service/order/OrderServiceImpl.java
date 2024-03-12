package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.constant.ShippoContants;
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
import com.shippo.Shippo;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import com.shippo.model.Rate;
import com.shippo.model.Shipment;
import com.shippo.model.Transaction;
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
    @Value("${shippo-test-key}")
    private String shippoTestKey;

    @Override
    @Transactional
    public CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {

        Shippo.setApiKey(shippoTestKey);

        HashMap<String, Object> addressToMap = new HashMap<>();


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
            addressToMap.put("name", user.getShippingInformation().getFirstName());


        } else {
            orderBuilder.cart(userSession.getCart());
            userSession.setCart(null);
            userSessionRepository.save(userSession);
            addressToMap.put("name", userSession.getCurrentShipping().getFirstName());
            addressToMap.put("company", "S3vers");

        }
        addressToMap.put("street1", "215 Clayton St.");
        addressToMap.put("city", "San Francisco");
        addressToMap.put("state", "CA");
        addressToMap.put("zip", "94117");
        addressToMap.put("country", "US");



// From Address
        HashMap<String, Object> addressFromMap = new HashMap<>();
        addressFromMap.put("name", ShippoContants.ADDRESS_FROM_COMPANY);
        addressFromMap.put("company", ShippoContants.ADDRESS_FROM_COMPANY);
        addressFromMap.put("street1", ShippoContants.ADDRESS_FROM);
        addressFromMap.put("city", ShippoContants.ADDRESS_FROM_CITY);
        addressFromMap.put("state", ShippoContants.ADDRESS_FROM_STATE);
        addressFromMap.put("zip", ShippoContants.ADDRESS_FROM_ZIP);
        addressFromMap.put("country", ShippoContants.ADDRESS_FROM_COUNTRY);

// Parcel
        HashMap<String, Object> parcelMap = new HashMap<>();
        parcelMap.put("length", "5");
        parcelMap.put("width", "5");
        parcelMap.put("height", "5");
        parcelMap.put("distance_unit", "in");
        parcelMap.put("weight", "2");
        parcelMap.put("mass_unit", "lb");

// Shipment
        HashMap<String, Object> shipmentMap = new HashMap<>();
        shipmentMap.put("address_to", addressToMap);
        shipmentMap.put("address_from", addressFromMap);
        shipmentMap.put("parcels", parcelMap);
        shipmentMap.put("async", false);
        Shipment shipment = Shipment.create(shipmentMap);

        List<Rate> rates = shipment.getRates();
        Rate rate = rates.get(0);

        Map<String, Object> transParams = new HashMap<>();
        transParams.put("rate", rate.getObjectId());
        transParams.put("async", false);
        Transaction transaction = Transaction.create(transParams);


        Order order = orderBuilder
                .tracking_id((String) transaction.getTrackingNumber())
                .status((String) transaction.getTrackingStatus())
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
                .orderId(order.getId())
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

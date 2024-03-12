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
    //    @Value("${shippo-test-key}")
    @Value("shippo_test_57350379cdf8b7a2b9fdf76ed0f677039321cae6")
    private String shippoTestKey;

    @Override
    @Transactional
    public CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {

        Shippo.setApiKey(shippoTestKey);
        Map<String, Object> toAddressMap = new HashMap<>();


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
            toAddressMap.put("name", user.getFirstName());
            toAddressMap.put("company", "Shippo");
            toAddressMap.put("street1", user.getShippingInformation().getAddress1());
            toAddressMap.put("city", user.getShippingInformation().getCity());
            toAddressMap.put("state", user.getShippingInformation().getState());
            toAddressMap.put("zip", user.getShippingInformation().getZipCode());
            toAddressMap.put("country", user.getShippingInformation().getCountry());
            toAddressMap.put("phone", "+1 555 341 9393");
            toAddressMap.put("email", user.getShippingInformation().getEmail());


        } else {
            orderBuilder.cart(userSession.getCart());
            userSession.setCart(null);
            userSessionRepository.save(userSession);
            toAddressMap.put("name", userSession.getCurrentShipping().getFirstName());
            toAddressMap.put("company", "Shippo");
            toAddressMap.put("street1", userSession.getCurrentShipping().getAddress1());
            toAddressMap.put("city", userSession.getCurrentShipping().getCity());
            toAddressMap.put("state", userSession.getCurrentShipping().getState());
            toAddressMap.put("zip", userSession.getCurrentShipping().getZipCode());
            toAddressMap.put("country", userSession.getCurrentShipping().getCountry());
            toAddressMap.put("phone", "+1 555 341 9393");
            toAddressMap.put("email", userSession.getCurrentShipping().getEmail());

        }


        // from address
        Map<String, Object> fromAddressMap = new HashMap<>();
        fromAddressMap.put("name", ShippoContants.ADDRESS_FROM_COMPANY);
        fromAddressMap.put("company", ShippoContants.ADDRESS_FROM_COMPANY);
        fromAddressMap.put("street1", ShippoContants.ADDRESS_FROM);
        fromAddressMap.put("city", ShippoContants.ADDRESS_FROM_CITY);
        fromAddressMap.put("state", ShippoContants.ADDRESS_FROM_STATE);
        fromAddressMap.put("zip", ShippoContants.ADDRESS_FROM_ZIP);
        fromAddressMap.put("country", ShippoContants.ADDRESS_FROM_COUNTRY);
        fromAddressMap.put("email", ShippoContants.ADDRESS_FROM_EMAIL);
        fromAddressMap.put("phone", ShippoContants.ADDRESS_FROM_PHONE);
        fromAddressMap.put("metadata", ShippoContants.ADDRESS_FROM_ID);

//        Map<String, Object> fromAddressMap = new HashMap<>();
//        fromAddressMap.put("name", "Ms Hippo");
//        fromAddressMap.put("company", "San Diego Zoo");
//        fromAddressMap.put("street1", "2920 Zoo Drive");
//        fromAddressMap.put("city", "San Diego");
//        fromAddressMap.put("state", "CA");
//        fromAddressMap.put("zip", "92101");
//        fromAddressMap.put("country", "US");
//        fromAddressMap.put("email", "mshippo@goshipppo.com");
//        fromAddressMap.put("phone", "+1 619 231 1515");
//        fromAddressMap.put("metadata", "Customer ID 123456");

        // parcel
        Map<String, Object> parcelMap = new HashMap<>();
        parcelMap.put("length", "5");
        parcelMap.put("width", "5");
        parcelMap.put("height", "5");
        parcelMap.put("distance_unit", "in");
        parcelMap.put("weight", "2");
        parcelMap.put("mass_unit", "lb");
        List<Map<String, Object>> parcels = new ArrayList<>();
        parcels.add(parcelMap);

        Map<String, Object> shipmentMap = new HashMap<>();
        shipmentMap.put("address_to", toAddressMap);
        shipmentMap.put("address_from", fromAddressMap);
        shipmentMap.put("parcels", parcels);
        shipmentMap.put("async", false);

        Shipment shipment = Shipment.create(shipmentMap);

        List<Rate> rates = shipment.getRates();
        Rate rate = rates.getFirst();

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

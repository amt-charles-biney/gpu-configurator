package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.constant.ShipmentContants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderStatusUpdate;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
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

        Map<String, Object> fromAddressMap = getFromAddressMap();
        Map<String, Object> toAddressMap = new HashMap<>();

        if (user != null) {
            orderBuilder.cart(user.getCart());
            user.setCart(null);
            userRepository.save(user);

            toAddressMap.put("name", user.getShippingInformation().getFirstName());
            toAddressMap.put("street1", user.getShippingInformation().getAddress1());
            toAddressMap.put("city", user.getShippingInformation().getCity());
            toAddressMap.put("state", user.getShippingInformation().getState());
            toAddressMap.put("country", user.getShippingInformation().getCountry());
            toAddressMap.put("zip", user.getShippingInformation().getZipCode());

        } else {
            orderBuilder.cart(userSession.getCart());
            userSession.setCart(null);
            userSessionRepository.save(userSession);

            toAddressMap.put("name", userSession.getCurrentShipping().getFirstName());
            toAddressMap.put("street1", userSession.getCurrentShipping().getAddress1());
            toAddressMap.put("city", userSession.getCurrentShipping().getCity());
            toAddressMap.put("state", userSession.getCurrentShipping().getState());
            toAddressMap.put("country", userSession.getCurrentShipping().getCountry());
            toAddressMap.put("zip", userSession.getCurrentShipping().getZipCode());

        }


        Map<String, Object> parcelMap = getParcelMap();

        Map<String, Object> shipmentMap = getShipmentMap(fromAddressMap, toAddressMap, parcelMap);

        Shipment shipment = client.shipment.create(shipmentMap);

        Shipment boughtShipment = client.shipment.buy(shipment.getId(), shipment.lowestRate());

        Order order = orderBuilder
                .trackingId((boughtShipment.getTracker().getTrackingCode()))
                .trackingUrl(boughtShipment.getTracker().getPublicUrl())
                .status(boughtShipment.getTracker().getStatus())
                .user(user)
                .payment(payment).build();
        orderRepository.save(order);

        return CreateOrderDto.builder()
                .orderId(order.getId())
                .trackingId(order.getTrackingId())
                .trackingUrl(order.getTrackingUrl())
                .build();
    }

    @NotNull
    private static Map<String, Object> getShipmentMap(Map<String, Object> fromAddressMap, Map<String, Object> toAddressMap, Map<String, Object> parcelMap) {
        Map<String, Object> shipmentMap = new HashMap<>();
        shipmentMap.put("from_address", fromAddressMap);
        shipmentMap.put("to_address", toAddressMap);
        shipmentMap.put("parcel", parcelMap);
        return shipmentMap;
    }

    @NotNull
    private static Map<String, Object> getParcelMap() {
        Map<String, Object> parcelMap = new HashMap<>();
        parcelMap.put("weight", 22.9);
        parcelMap.put("height", 12.1);
        parcelMap.put("width", 8);
        parcelMap.put("length", 19.8);
        return parcelMap;
    }

    @NotNull
    private static Map<String, Object> getFromAddressMap() {
        Map<String, Object> fromAddressMap = new HashMap<>();
        fromAddressMap.put("company", ShipmentContants.ADDRESS_FROM_COMPANY);
        fromAddressMap.put("street1", ShipmentContants.ADDRESS_FROM);
        fromAddressMap.put("city", ShipmentContants.ADDRESS_FROM_CITY);
        fromAddressMap.put("state", ShipmentContants.ADDRESS_FROM_STATE);
        fromAddressMap.put("country", ShipmentContants.ADDRESS_FROM_COUNTRY);
        fromAddressMap.put("zip", ShipmentContants.ADDRESS_FROM_ZIP);
        return fromAddressMap;
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

    @Override
    public GenericResponse updateStatus(UUID id, OrderStatusUpdate status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(status.status());
        orderRepository.save(order);
        return GenericResponse.builder()
                .status(200)
                .message("order id" + " " + id + " " + "updated")
                .build();
    }

    private OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderId(order.getTrackingId())
                .configuredProduct(order.getCart().getConfiguredProducts())
                .productCoverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .productName(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductName()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .status(order.getStatus())
                .trackingUrl(order.getTrackingUrl())
                .customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .totalPrice(order.getCart().getConfiguredProducts().stream()
                        .map(Configuration::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .date(order.getCreatedAt())
                .build();
    }


}

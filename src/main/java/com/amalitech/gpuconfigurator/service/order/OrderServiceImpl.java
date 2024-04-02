package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.constant.ShipmentContants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderStatusUpdate;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.status.StatusService;
import com.easypost.exception.EasyPostException;
import com.easypost.exception.General.MissingParameterError;
import com.easypost.model.Shipment;
import com.easypost.service.EasyPostClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final CompatibleOptionRepository compatibleOptionRepository;
    private final AttributeOptionRepository attributeOption;
    @Value("${easy-test-key}")
    private String easyPost;

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
                user.setCart(new Cart());
                userRepository.save(user);
                orderBuilder.user(user);

            } else {
                orderBuilder.cart(userSession.getCart());
                userSession.setCart(new Cart());
                userSessionRepository.save(userSession);
                orderBuilder.userSession(userSession);

            }


            Order order = orderBuilder
                    .trackingId(null)
                    .trackingUrl(null)
                    .status("order confirmation")
                    .estDeliveryDate(null)
                    .payment(payment).build();
            orderRepository.save(order);

            reduceStock(order.getCart());

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
        Pageable pageable = getPageable(page, size);
        return orderRepository.findAll(pageable).map(this::mapOrderToOrderResponseDto);
    }

    @Override
    public Page<OrderResponseDto> getAllUserOrders(Integer page, Integer size, Principal principal) {
        Pageable pageable = getPageable(page, size);

        if (principal == null) throw new UsernameNotFoundException("user cannot be found");

        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return orderRepository.findAllByUser(user, pageable).map(this::mapOrderToOrderResponseDto);
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

    public Order getOrderByTrackingCode(String trackingCode) {
        return orderRepository.findByTrackingId(trackingCode).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public GenericResponse updateStatusByTrackingCode(String trackingCode, String status, String deliveryDate) {
        Order order = this.getOrderByTrackingCode(trackingCode);
        order.setStatus(status);
        order.setEstDeliveryDate(deliveryDate);
        orderRepository.save(order);
        return GenericResponse.builder()
                .status(200)
                .message("order id" + " " + order.getId() + " " + "updated")
                .build();
    }

    @Override
    public void shipment(UUID orderId) throws EasyPostException {
        EasyPostClient client = new EasyPostClient(easyPost);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("No order with this Id"));

        Map<String, Object> fromAddressMap = getFromAddressMap();
        Map<String, Object> toAddressMap = new HashMap<>();

        if (order.getUser() != null) {
            toAddressMap.put("name", order.getUser().getShippingInformation().getFirstName());
            toAddressMap.put("street1", order.getUser().getShippingInformation().getAddress1());
            toAddressMap.put("city", order.getUser().getShippingInformation().getCity());
            toAddressMap.put("state", order.getUser().getShippingInformation().getState());
            toAddressMap.put("country", order.getUser().getShippingInformation().getCountry());
            toAddressMap.put("zip", order.getUser().getShippingInformation().getZipCode());
        } else {
            toAddressMap.put("name", order.getUserSession().getCurrentShipping().getFirstName());
            toAddressMap.put("street1", order.getUserSession().getCurrentShipping().getAddress1());
            toAddressMap.put("city", order.getUserSession().getCurrentShipping().getCity());
            toAddressMap.put("state", order.getUserSession().getCurrentShipping().getState());
            toAddressMap.put("country", order.getUserSession().getCurrentShipping().getCountry());
            toAddressMap.put("zip", order.getUserSession().getCurrentShipping().getZipCode());
        }

        Map<String, Object> parcelMap = getParcelMap();

        Map<String, Object> shipmentMap = getShipmentMap(fromAddressMap, toAddressMap, parcelMap);

        Shipment shipment = client.shipment.create(shipmentMap);

        Shipment boughtShipment = client.shipment.buy(shipment.getId(), shipment.lowestRate());

        order.setTrackingId(boughtShipment.getTracker().getTrackingCode());
        order.setTrackingUrl(boughtShipment.getTracker().getPublicUrl());
        order.setStatus(StatusService.mapEasyPostStatus(boughtShipment.getTracker().getStatus()));
        order.setEstDeliveryDate(boughtShipment.getTracker().getEstDeliveryDate() != null ? boughtShipment.getTracker().getEstDeliveryDate().toString() : null);
        orderRepository.save(order);

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
                .estArrival(order.getEstDeliveryDate())
                .brandName(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getName()).orElse(null))
                .shippingAddress(order.getUser().getShippingInformation().getAddress1())
                .build();
    }


    @NotNull
    private static Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }

    private void reduceStock(Cart cart) {
        Set<Configuration> configuredProducts = cart.getConfiguredProducts();

        List<UUID> allOptionIds = configuredProducts.stream()
                .flatMap(configuration -> configuration.getConfiguredOptions().stream())
                .map(ConfigOptions::getOptionId)
                .map(UUID::fromString)
                .toList();
        List<CompatibleOption> results = compatibleOptionRepository.findAllIdsIn(allOptionIds);
        for (CompatibleOption result : results) {

            for (Configuration x : configuredProducts) {
                var inStockVar = result.getAttributeOption().getInStock();
                result.getAttributeOption().setInStock(inStockVar - x.getQuantity());
                attributeOption.save(result.getAttributeOption());
            }
        }
    }

}

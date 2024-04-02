package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderStatusUpdate;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import com.easypost.exception.EasyPostException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    @Transactional
    CreateOrderDto createOrder(Payment payment, Principal principal, UserSession userSession);

    Page<OrderResponseDto> getAllOrders(Integer page, Integer size);


    Page<OrderResponseDto> getAllUserOrders(Integer page, Integer size, Principal principal);

    OrderResponseDto getOrderById(UUID id);

    @Transactional
    GenericResponse deleteBulkProducts(List<String> ids);

    GenericResponse updateStatus(UUID id, OrderStatusUpdate status);

    GenericResponse updateStatusByTrackingCode(String trackingCode, String status, String deliveryDate);

    void shipment(UUID orderId) throws EasyPostException;
}

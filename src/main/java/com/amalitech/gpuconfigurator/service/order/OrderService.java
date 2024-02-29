package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.order.CreateOrderRequestDto;


import java.security.Principal;

public interface OrderService {
    GenericResponse createOrder(CreateOrderRequestDto request, Principal principal, UserSession userSession);
}

package com.amalitech.gpuconfigurator.service.order;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.payment.Payment;
import jakarta.transaction.Transactional;

import java.security.Principal;

public interface OrderService {
    @Transactional
    GenericResponse createOrder(Payment payment, Principal principal, UserSession userSession);
}

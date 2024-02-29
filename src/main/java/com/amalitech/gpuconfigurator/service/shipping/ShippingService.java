package com.amalitech.gpuconfigurator.service.shipping;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.model.UserSession;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.UUID;

public interface ShippingService {
    ShippingResponse create(ShippingRequest dto, Principal principal, UserSession userSession);

    Page<ShippingResponse> findAll(int page, int size);

    ShippingResponse findById(UUID shippingId);

    ShippingResponse update(UUID shippingId, ShippingRequest dto);

    GenericResponse delete(UUID shippingId);
}

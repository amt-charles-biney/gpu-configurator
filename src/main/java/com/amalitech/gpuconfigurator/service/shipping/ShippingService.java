package com.amalitech.gpuconfigurator.service.shipping;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.easypost.exception.EasyPostException;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ShippingService {
    ShippingResponse create(ShippingRequest dto, User currentUser, UserSession userSession) throws EasyPostException;

    Page<ShippingResponse> findAll(int page, int size);

    ShippingResponse findById(UUID shippingId);

    ShippingResponse update(UUID shippingId, ShippingRequest dto);

    GenericResponse delete(UUID shippingId);
}

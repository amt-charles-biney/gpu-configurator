package com.amalitech.gpuconfigurator.dto.shipping;

import com.amalitech.gpuconfigurator.dto.profile.ContactResponse;

import java.util.UUID;

public interface ShippingResponse {
    UUID getId();

    String getFirstName();

    String getLastName();

    String getAddress1();

    String getAddress2();

    String getCountry();

    String getState();

    String getCity();

    String getZipCode();

    String getEmail();

    ContactResponse getContact();
}

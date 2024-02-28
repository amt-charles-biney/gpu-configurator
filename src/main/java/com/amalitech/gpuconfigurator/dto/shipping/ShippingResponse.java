package com.amalitech.gpuconfigurator.dto.shipping;

import com.amalitech.gpuconfigurator.dto.profile.ContactResponse;

public interface ShippingResponse {
    String getFirstName();

    String getLastName();

    String getAddress1();

    String getAddress2();

    String getCountry();

    String getState();

    String getCity();

    String getZipCode();

    ContactResponse getContact();
}

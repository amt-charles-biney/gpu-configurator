package com.amalitech.gpuconfigurator.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileShippingResponse {
    private UUID id;

    private String firstName;

    private String lastName;

    private String address1;

    private String address2;

    private String country;

    private String state;

    private String city;

    private String zipCode;

    private String email;

    private ProfileContactResponse contact;
}

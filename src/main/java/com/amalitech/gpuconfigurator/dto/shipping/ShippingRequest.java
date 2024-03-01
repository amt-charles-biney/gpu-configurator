package com.amalitech.gpuconfigurator.dto.shipping;

import com.amalitech.gpuconfigurator.annotation.OptionalAddress;
import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address1;

    @OptionalAddress
    private String address2;

    @NotBlank
    private String country;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    @NotBlank
    private String zipCode;

    @NotBlank
    @Email
    private String email;

    @Valid
    private ContactRequest contact;

    @NotNull
    private boolean saveInformation = false;
}

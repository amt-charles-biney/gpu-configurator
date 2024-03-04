package com.amalitech.gpuconfigurator.dto.profile;

import com.amalitech.gpuconfigurator.annotation.OptionalAddress;
import jakarta.validation.Valid;
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
public class ProfileShippingRequest {
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

    private String email;

    @Valid
    @NotNull
    private ContactRequest contact;
}

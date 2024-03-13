package com.amalitech.gpuconfigurator.dto.shipping;

public record AddressRequestDto(
        String street1,
        String city, // should be the city code eg: SF for San Franscisco
        String state, // should be the state code eg: CA
        String zip,
        String country, //// should be the Country code eg: US for United State

        String company,

        String phone
) {
}

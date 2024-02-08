package com.amalitech.gpuconfigurator.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactResponse {
    private String phoneNumber;

    private String country;

    private String iso2Code;

    private String dialCode;
}

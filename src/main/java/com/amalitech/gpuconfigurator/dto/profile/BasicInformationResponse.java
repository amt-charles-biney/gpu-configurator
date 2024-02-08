package com.amalitech.gpuconfigurator.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicInformationResponse {
    private String firstName;

    private String lastName;

    private String email;

    private ContactResponse contact;
}

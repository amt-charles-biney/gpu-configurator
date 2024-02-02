package com.amalitech.gpuconfigurator.dto.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicInformationRequest {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @Valid
    @NotNull
    public ContactRequest contact;
}

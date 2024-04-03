package com.amalitech.gpuconfigurator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ShipmentDto {
    private UUID id;
}

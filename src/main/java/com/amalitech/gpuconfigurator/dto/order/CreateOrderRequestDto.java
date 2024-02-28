package com.amalitech.gpuconfigurator.dto.order;

import com.amalitech.gpuconfigurator.model.OrderType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    @NotBlank
    OrderType status;

    @NotBlank
    UUID userId;

    @NotBlank
    List<UUID> paymentId;
}

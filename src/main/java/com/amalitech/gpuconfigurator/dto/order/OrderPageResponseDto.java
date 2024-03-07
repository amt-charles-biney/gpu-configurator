package com.amalitech.gpuconfigurator.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponseDto {
    private List<OrderResponseDto> orders;
    private long total;
}


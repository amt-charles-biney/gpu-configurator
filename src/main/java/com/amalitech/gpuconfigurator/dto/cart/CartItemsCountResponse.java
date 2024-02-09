package com.amalitech.gpuconfigurator.dto.cart;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsCountResponse {
    private long count;
}

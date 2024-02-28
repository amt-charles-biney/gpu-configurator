package com.amalitech.gpuconfigurator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct implements Serializable {
    private String productName;

    private String coverImage;

    private BigDecimal totalPrice;
}

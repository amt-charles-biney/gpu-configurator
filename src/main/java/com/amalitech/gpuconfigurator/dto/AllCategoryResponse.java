package com.amalitech.gpuconfigurator.dto;


import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoryResponse {
    private UUID id;
    private String categoryName;
}

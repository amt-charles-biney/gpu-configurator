package com.amalitech.gpuconfigurator.dto.cases;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseResponse {
    private UUID id;

    private String name;

    private String description;

    private String coverImageUrl;

    private List<String> imageUrls;

    private BigDecimal price;

    private List<AttributeOption> incompatibleVariants;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

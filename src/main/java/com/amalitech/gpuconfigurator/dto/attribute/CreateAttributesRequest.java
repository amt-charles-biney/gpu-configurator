package com.amalitech.gpuconfigurator.dto.attribute;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record CreateAttributesRequest(String attributeName, Boolean isMeasured, String description, String unit, Boolean isRequired,  List<CreateAttributeOptionRequest> variantOptions) {
}

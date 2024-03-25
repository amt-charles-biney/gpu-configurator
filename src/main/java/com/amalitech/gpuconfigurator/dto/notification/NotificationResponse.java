package com.amalitech.gpuconfigurator.dto.notification;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record NotificationResponse(int count, List<AttributeResponse> attributeResponseList) {
}

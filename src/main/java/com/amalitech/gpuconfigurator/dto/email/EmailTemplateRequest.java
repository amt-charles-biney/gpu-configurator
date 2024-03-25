package com.amalitech.gpuconfigurator.dto.email;

import lombok.Builder;

@Builder
public record EmailTemplateRequest(String to, String title, String message, String templateString) {
}

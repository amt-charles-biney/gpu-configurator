package com.amalitech.gpuconfigurator.dto.cases;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public interface UserCaseResponse {
    UUID getId();

    String getName();

    String getDescription();

    @JsonProperty("thumbnail")
    String getCoverImageUrl();
}

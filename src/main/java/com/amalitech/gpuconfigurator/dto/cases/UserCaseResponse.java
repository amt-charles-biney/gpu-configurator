package com.amalitech.gpuconfigurator.dto.cases;

import java.util.UUID;

public interface UserCaseResponse {
    UUID getId();

    String getName();

    String getDescription();

    String getCoverImageUrl();
}

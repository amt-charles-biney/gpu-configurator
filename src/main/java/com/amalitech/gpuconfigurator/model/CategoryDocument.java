package com.amalitech.gpuconfigurator.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDocument {
    private UUID id;

    private String name;
}

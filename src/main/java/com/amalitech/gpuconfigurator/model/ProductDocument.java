package com.amalitech.gpuconfigurator.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(indexName = "products")
public class ProductDocument {
    private UUID id;

    private String productName;

    private String productId;

    private String productDescription;

    private Double productPrice;

    private String productBrand;

    private Integer inStock;

    private Boolean featured;

    private List<String> imageUrl;

    private String coverImage;

    private Boolean productAvailability;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime deletedAt;
}
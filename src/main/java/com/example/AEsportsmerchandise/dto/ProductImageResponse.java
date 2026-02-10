package com.example.AEsportsmerchandise.dto;

import lombok.Data;

@Data
public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private String publicId;
    private Boolean isPrimary;
    private Integer sortOrder;

    // ✅ NEW FIELD
    private String sku;
}
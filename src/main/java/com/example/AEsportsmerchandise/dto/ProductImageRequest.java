package com.example.AEsportsmerchandise.dto;

import lombok.Data;

@Data
public class ProductImageRequest {

    private String imageUrl;
    private String publicId;
    private Boolean isPrimary;
    private Integer sortOrder;

    // ❌ remove variantId for create
    // private Long variantId;

    // ✅ NEW
    private String color;
    private String size;
}
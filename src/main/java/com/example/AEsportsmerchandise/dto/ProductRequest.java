// ProductRequest.java
package com.example.AEsportsmerchandise.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String shortDescription;
    private String category;
    private String brand;
    private String countryCode;

    private BigDecimal basePrice;
    private BigDecimal discountPrice;
    private Boolean returnable;
    private Integer returnWindowDays;
    private List<ProductVariantRequest> variants;
    private List<ProductImageRequest> images;
}
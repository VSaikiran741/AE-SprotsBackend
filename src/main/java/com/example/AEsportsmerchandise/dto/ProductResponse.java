// ProductResponse.java
package com.example.AEsportsmerchandise.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String slug;
    private String name;
    private String description;
    private String shortDescription;
    private String category;
    private String brand;
    private BigDecimal basePrice;
    private BigDecimal discountPrice;
    private Integer discountPercent;
    private BigDecimal rating;
    private Integer reviewCount;
    private Boolean active;
    private Boolean returnable;
    private Integer returnWindowDays;
    private List<ProductVariantResponse> variants;
    private List<ProductImageResponse> images;
}
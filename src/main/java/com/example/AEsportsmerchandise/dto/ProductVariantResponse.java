// ProductVariantResponse.java
package com.example.AEsportsmerchandise.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductVariantResponse {
    private Long id;
    private String size;
    private String color;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
}
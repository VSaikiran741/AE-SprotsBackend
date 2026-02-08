// ProductVariantRequest.java
package com.example.AEsportsmerchandise.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductVariantRequest {
    private String size;
    private String color;
    private BigDecimal price;
    private Integer stock;
//    private Integer reservedStock;
//    private BigDecimal basePrice;
//    private BigDecimal discountPrice;
//    private Integer discountPercent;
//}
}
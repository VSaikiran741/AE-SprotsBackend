package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemResponse {
    private Long itemId;
    private Long variantId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
//    private Boolean selected;

}

package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    private Long variantId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}


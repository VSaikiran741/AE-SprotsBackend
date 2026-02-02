package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequest {

    private Long variantId;
    private Integer quantity;
}

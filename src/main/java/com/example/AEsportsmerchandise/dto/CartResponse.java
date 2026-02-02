package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;
}

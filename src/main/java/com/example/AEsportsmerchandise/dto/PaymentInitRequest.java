package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInitRequest {
    private Long orderId;
    private String method;
}

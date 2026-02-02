package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentInitiateResponse {
    private Long orderId;
    private String method;
    private BigDecimal amount;
    private String status;
}


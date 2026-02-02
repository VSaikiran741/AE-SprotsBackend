package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusResponse {
    private Long orderId;
    private String paymentStatus;
}

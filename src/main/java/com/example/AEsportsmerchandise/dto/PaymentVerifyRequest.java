package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequest {
    private String gatewayPaymentId;
    private boolean success;
}

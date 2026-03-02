package com.example.AEsportsmerchandise.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {

    private Long id;
    private Long orderId;

    private BigDecimal amount;

    private String status;
    private String method;

    private String gatewayOrderId;
    private String gatewayPaymentId;

    private Integer attemptCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

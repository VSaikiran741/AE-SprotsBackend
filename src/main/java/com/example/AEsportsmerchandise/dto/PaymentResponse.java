package com.example.AEsportsmerchandise.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private String status;        // INITIATED, SUCCESS, FAILED, REFUNDED

    private String method;        // COD, UPI, CARD

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private Integer attemptCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

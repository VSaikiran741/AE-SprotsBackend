package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.PaymentInitRequest;
import com.example.AEsportsmerchandise.dto.PaymentInitiateResponse;
import com.example.AEsportsmerchandise.dto.PaymentStatusResponse;
import com.example.AEsportsmerchandise.dto.PaymentVerifyRequest;
import com.example.AEsportsmerchandise.service.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Payments",
        description = "Payment initiation and verification APIs"
)
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Initiate payment",
            description = "Initiates payment for an order using the selected payment method"
    )
    @ApiResponse(responseCode = "200", description = "Payment initiated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid payment request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping("/initiate")
    @PreAuthorize("hasRole('USER')")
    public PaymentInitiateResponse initiate(
            @RequestBody PaymentInitRequest req
    ) {
        return paymentService.initiatePayment(
                req.getOrderId(),
                req.getMethod()
        );
    }

    @Operation(
            summary = "Verify payment",
            description = "Verifies payment details after completion with the payment gateway"
    )
    @ApiResponse(responseCode = "200", description = "Payment verified successfully")
    @ApiResponse(responseCode = "400", description = "Payment verification failed")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PostMapping("/verify/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public void verify(
            @PathVariable Long orderId,
            @RequestBody PaymentVerifyRequest request
    ) {
        paymentService.verifyPayment(orderId, request);
    }

    @Operation(
            summary = "Get payment status",
            description = "Returns the current payment status for a given order"
    )
    @ApiResponse(responseCode = "200", description = "Payment status fetched successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public PaymentStatusResponse status(@PathVariable Long orderId) {
        return paymentService.getPaymentStatus(orderId);
    }
}

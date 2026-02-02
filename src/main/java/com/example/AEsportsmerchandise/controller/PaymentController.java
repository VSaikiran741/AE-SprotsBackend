package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.*;
import com.example.AEsportsmerchandise.entity.PaymentMethod;
import com.example.AEsportsmerchandise.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public PaymentInitiateResponse initiate(@RequestBody PaymentInitRequest req) {
        return paymentService.initiatePayment(
                req.getOrderId(),
                req.getMethod()
        );
    }

    @PostMapping("/verify/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void verify(
            @PathVariable Long orderId,
            @RequestBody PaymentVerifyRequest request
    ) {
        paymentService.verifyPayment(orderId, request);
    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public PaymentStatusResponse status(@PathVariable Long orderId) {
        return paymentService.getPaymentStatus(orderId);
    }
}

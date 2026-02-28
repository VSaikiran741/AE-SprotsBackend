package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.PaymentInitiateResponse;
import com.example.AEsportsmerchandise.dto.PaymentResponse;
import com.example.AEsportsmerchandise.dto.PaymentStatusResponse;
import com.example.AEsportsmerchandise.dto.PaymentVerifyRequest;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductVariantRepository variantRepository;
    public PaymentStatusResponse getPaymentStatus(Long orderId) {

        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentStatusResponse response = new PaymentStatusResponse();
        response.setOrderId(orderId);
        response.setPaymentStatus(payment.getStatus().name());

        return response;
    }
    // ================= GET ALL PAYMENTS (ADMIN) =================
    public List<PaymentResponse> getAllPayments() {

        List<PaymentEntity> payments = paymentRepository.findAll();

        List<PaymentResponse> responses = new ArrayList<>();

        for (PaymentEntity payment : payments) {
            responses.add(toPaymentResponse(payment));
        }

        return responses;
    }

    private PaymentResponse toPaymentResponse(PaymentEntity payment) {

        PaymentResponse dto = new PaymentResponse();

        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());

        dto.setStatus(payment.getStatus().name());
        dto.setMethod(payment.getMethod().name());

        dto.setGatewayOrderId(payment.getGatewayOrderId());
        dto.setGatewayPaymentId(payment.getGatewayPaymentId());

        dto.setAttemptCount(payment.getAttemptCount());

        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        return dto;
    }

    @Transactional
    public PaymentInitiateResponse initiatePayment(Long orderId, String method) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(order);
        payment.setMethod(paymentMethod);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.INITIATED);

        paymentRepository.save(payment);

        PaymentInitiateResponse response = new PaymentInitiateResponse();
        response.setOrderId(orderId);
        response.setMethod(paymentMethod.name());
        response.setAmount(order.getTotalAmount());
        response.setStatus(payment.getStatus().name());

        return response;
    }
    @Transactional
    public void verifyPayment(Long orderId, PaymentVerifyRequest request) {

        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow();

        if (payment.getStatus() == PaymentStatus.SUCCESS) return;

        OrderEntity order = payment.getOrder();

        if (payment.getAttemptCount() >= 3) {
            throw new RuntimeException("Payment attempts exceeded");
        }

        payment.setAttemptCount(payment.getAttemptCount() + 1);

        if (request.isSuccess()) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setGatewayPaymentId(request.getGatewayPaymentId());
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }
    }

    @Transactional
    public void markPaymentSuccess(Long orderId, String gatewayPaymentId) {

        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Idempotent: if already successful, do nothing
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        // Update payment
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(gatewayPaymentId);

        // Update order
        OrderEntity order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);
    }


    @Transactional
    public void markPaymentFailed(Long orderId) {

        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // If already failed, do nothing
        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        // Update payment
        payment.setStatus(PaymentStatus.FAILED);

        // Update order
        OrderEntity order = payment.getOrder();
        order.setStatus(OrderStatus.PAYMENT_FAILED);
    }

}

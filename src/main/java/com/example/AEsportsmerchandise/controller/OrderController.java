package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.*;
import com.example.AEsportsmerchandise.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public OrderDetailResponse placeOrder() {
        return orderService.placeOrder();
    }

    @PostMapping("/place-item/{cartItemId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public OrderDetailResponse placeSingleItemOrder(
            @PathVariable Long cartItemId
    ) {
        return orderService.placeSingleItemOrder(cartItemId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<OrderSummaryResponse> myOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public OrderDetailResponse orderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<OrderItemResponse> orderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }
}

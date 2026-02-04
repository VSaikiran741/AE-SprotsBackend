package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.OrderDetailResponse;
import com.example.AEsportsmerchandise.dto.OrderItemResponse;
import com.example.AEsportsmerchandise.dto.OrderSummaryResponse;
import com.example.AEsportsmerchandise.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Orders",
        description = "Order placement and tracking APIs (USER role required)"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Place order from cart",
            description = "Places an order using all items currently present in the user's cart"
    )
    @ApiResponse(responseCode = "200", description = "Order placed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public OrderDetailResponse placeOrder() {
        return orderService.placeOrder();
    }

    @Operation(
            summary = "Place order for a single cart item",
            description = "Places an order using a single cart item identified by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Single-item order placed successfully")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    @PostMapping("/place-item/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderDetailResponse placeSingleItemOrder(
            @PathVariable Long cartItemId
    ) {
        return orderService.placeSingleItemOrder(cartItemId);
    }

    @Operation(
            summary = "Get my orders",
            description = "Returns a list of all orders placed by the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<OrderSummaryResponse> myOrders() {
        return orderService.getMyOrders();
    }

    @Operation(
            summary = "Get order details",
            description = "Returns detailed information for a specific order"
    )
    @ApiResponse(responseCode = "200", description = "Order details fetched successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public OrderDetailResponse orderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @Operation(
            summary = "Get order items",
            description = "Returns all items associated with a specific order"
    )
    @ApiResponse(responseCode = "200", description = "Order items fetched successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemResponse> orderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }
}

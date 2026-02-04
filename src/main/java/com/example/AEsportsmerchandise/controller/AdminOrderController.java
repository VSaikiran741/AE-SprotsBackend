package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Admin - Orders",
        description = "Admin order management APIs"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Cancel order",
            description = "Cancels an order if it has not yet been shipped (Admin only)"
    )
    @ApiResponse(responseCode = "200", description = "Order cancelled successfully")
    @ApiResponse(responseCode = "400", description = "Order cannot be cancelled")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PutMapping("/{id}/cancel")
    public void cancelOrder(
            @PathVariable Long id
    ) {
        orderService.cancelOrder(id);
    }
}

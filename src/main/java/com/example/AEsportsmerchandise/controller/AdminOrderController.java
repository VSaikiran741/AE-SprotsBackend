package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // ❌ Cancel order (only before shipping)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }


}

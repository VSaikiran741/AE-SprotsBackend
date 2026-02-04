package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.CartAddRequest;
import com.example.AEsportsmerchandise.dto.CartResponse;
import com.example.AEsportsmerchandise.dto.CartUpdateRequest;
import com.example.AEsportsmerchandise.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Cart",
        description = "User cart management APIs (USER role required)"
)
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Get current user's cart",
            description = "Returns the cart details of the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Cart fetched successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public CartResponse getCart() {
        return cartService.getCartResponse();
    }

    @Operation(
            summary = "Add item to cart",
            description = "Adds a product variant to the user's cart"
    )
    @ApiResponse(responseCode = "200", description = "Item added to cart")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addToCart(@RequestBody CartAddRequest request) {
        cartService.addToCart(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update cart item quantity",
            description = "Updates the quantity of an existing cart item"
    )
    @ApiResponse(responseCode = "200", description = "Cart item updated")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    @PutMapping("/update/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateQuantity(
            @PathVariable Long itemId,
            @RequestBody CartUpdateRequest request
    ) {
        cartService.updateQuantity(itemId, request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove item from cart",
            description = "Removes a specific item from the user's cart"
    )
    @ApiResponse(responseCode = "204", description = "Item removed from cart")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    @DeleteMapping("/remove/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Clear cart",
            description = "Removes all items from the current user's cart"
    )
    @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCurrentUserCart();
        return ResponseEntity.noContent().build();
    }
}

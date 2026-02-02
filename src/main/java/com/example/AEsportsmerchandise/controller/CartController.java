package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.CartAddRequest;
import com.example.AEsportsmerchandise.dto.CartResponse;
import com.example.AEsportsmerchandise.dto.CartUpdateRequest;
import com.example.AEsportsmerchandise.entity.CartEntity;
import com.example.AEsportsmerchandise.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CartResponse getCart() {
        return cartService.getCartResponse();
    }

//    @DeleteMapping("/items/{cartItemId}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public void removeItem(@PathVariable Long  ItemId) {
//        cartService.removeItem(ItemId);
//    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void addToCart(@RequestBody CartAddRequest request) {
        cartService.addToCart(request);
    }

    @PutMapping("/update/{itemId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updateQuantity(
            @PathVariable Long itemId,
            @RequestBody CartUpdateRequest request
    ) {
        cartService.updateQuantity(itemId, request.getQuantity());
    }

    @DeleteMapping("/remove/{itemId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
    }
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCurrentUserCart();
        return ResponseEntity.noContent().build();
    }
}

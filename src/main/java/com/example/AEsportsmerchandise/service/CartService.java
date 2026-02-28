package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.CartAddRequest;
import com.example.AEsportsmerchandise.dto.CartItemResponse;
import com.example.AEsportsmerchandise.dto.CartResponse;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;

    private UserEntity currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }

    @Transactional
    public CartEntity getCart() {
        UserEntity user = currentUser();

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity cart = new CartEntity();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
    @Transactional
    public CartResponse getCartResponse() {

        CartEntity cart = getCart();

        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());

        List<CartItemResponse> items = new ArrayList<>();

        for (CartItemEntity item : cart.getItems()) {

            CartItemResponse dto = new CartItemResponse();
            dto.setItemId(item.getId());
            dto.setVariantId(item.getVariant().getId());
            dto.setProductName(item.getVariant().getProduct().getName());
            dto.setPrice(item.getVariant().getPrice());
            dto.setQuantity(item.getQuantity());

            items.add(dto);
        }

        response.setItems(items);
        return response;
    }
//    @Transactional
//    public void removeItem(Long ItemId) {
//
//        UserEntity user = currentUser();
//
//        CartItemEntity item = cartItemRepository.findById(ItemId)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        // Safety check: user owns this cart
//        if (!item.getCart().getUser().getId().equals(user.getId())) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        item.getCart().getItems().remove(item);
//        cartItemRepository.delete(item);
//    }



    @Transactional
    public void addToCart(CartAddRequest request) {

        CartEntity cart = getCart();

        ProductVariantEntity variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        CartItemEntity item = cartItemRepository
                .findByCartIdAndVariantId(cart.getId(), variant.getId())
                .orElse(null);

        int alreadyInCart = (item == null) ? 0 : item.getQuantity();
        int requestedTotal = alreadyInCart + request.getQuantity();

        int available = variant.getStock() - variant.getReservedStock();
        if (requestedTotal > available) {
            throw new RuntimeException(
                    "Only " + available + " items available for SKU: " + variant.getSku()
            );
        }

        if (item == null) {
            item = new CartItemEntity();
            item.setCart(cart);
            item.setVariant(variant);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        } else {
            item.setQuantity(requestedTotal);
        }
    }

    @Transactional
    public void updateQuantity(Long itemId, Integer quantity) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
        }
    }

    @Transactional
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCurrentUserCart() {

        UserEntity user = currentUser(); // from SecurityContext

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
    }
//    public int getCartItemCount() {
//
//        UserEntity user = getCurrentUser();
//
//        return cartItemRepository.countByUser_Id(user.getId());
//    }

//    // ================= TOGGLE CART ITEM SELECTION =================
//    @Transactional
//    public void toggleSelection(Long itemId) {
//
//        UserEntity user = getCurrentUser();
//
//        CartItemEntity item = cartItemRepository
//                .findByIdAndUser_Id(itemId, user.getId())
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        item.setSelected(!item.getSelected());
//    }
//    private UserEntity getCurrentUser() {
//
//        Authentication auth =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        return (UserEntity) auth.getPrincipal();
//    }
//

}

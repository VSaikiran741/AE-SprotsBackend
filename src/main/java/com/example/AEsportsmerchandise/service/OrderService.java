package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.*;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;

    /* =========================
       AUTH HELPER
       ========================= */
    private UserEntity currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated request");
        }

        Object principal = auth.getPrincipal();

        if (!(principal instanceof UserEntity user)) {
            throw new RuntimeException("Invalid authentication principal");
        }

        return user;
    }

    /* =========================
       PLACE FULL CART ORDER
       ========================= */
    @Transactional
    public OrderDetailResponse placeOrder() {

        UserEntity user = currentUser();

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemEntity cartItem : cart.getItems()) {

            ProductVariantEntity variant = cartItem.getVariant();

            // STOCK CHECK
            if (variant.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for SKU: " + variant.getSku()
                );
            }

            // DECREASE STOCK (FINAL SALE)
            variant.setStock(
                    variant.getStock() - cartItem.getQuantity()
            );
            productVariantRepository.save(variant);

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setVariant(variant);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(variant.getPrice());

            orderItemRepository.save(orderItem);

            total = total.add(
                    variant.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        // clear cart
        cart.getItems().clear();

        return getOrderDetails(order.getId());
    }

    /* =========================
       PLACE SINGLE ITEM ORDER
       ========================= */
    @Transactional
    public OrderDetailResponse placeSingleItemOrder(Long cartItemId) {

        UserEntity user = currentUser();

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItemEntity cartItem = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        ProductVariantEntity variant = cartItem.getVariant();

        if (variant.getStock() < cartItem.getQuantity()) {
            throw new RuntimeException("Insufficient stock for SKU: " + variant.getSku());
        }

        // ✅ Calculate total BEFORE saving order
        BigDecimal total = variant.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        // ✅ Create order with totalAmount set
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(total);

        order = orderRepository.save(order); // ✅ safe now

        // ✅ Reduce stock
        variant.setStock(variant.getStock() - cartItem.getQuantity());
        productVariantRepository.save(variant);

        // Create order item
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(order);
        orderItem.setVariant(variant);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(variant.getPrice());

        orderItemRepository.save(orderItem);

        // Remove cart item
        cart.getItems().remove(cartItem);

        return getOrderDetails(order.getId());
    }


    /* =========================
       ORDER DETAILS
       ========================= */
    @Transactional
    public OrderDetailResponse getOrderDetails(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemEntity> items =
                orderItemRepository.findByOrderIdWithVariant(orderId);

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItemEntity item : items) {
            OrderItemResponse dto = new OrderItemResponse();
            dto.setVariantId(item.getVariant().getId());
            dto.setProductName(item.getVariant().getProduct().getName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            itemResponses.add(dto);
        }

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(itemResponses);

        return response;
    }

    /* =========================
       MY ORDERS
       ========================= */
    @Transactional
    public List<OrderSummaryResponse> getMyOrders() {

        UserEntity user = currentUser();

        List<OrderEntity> orders = orderRepository.findByUser(user);
        List<OrderSummaryResponse> responses = new ArrayList<>();

        for (OrderEntity order : orders) {
            OrderSummaryResponse dto = new OrderSummaryResponse();
            dto.setOrderId(order.getId());
            dto.setStatus(order.getStatus().name());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            responses.add(dto);
        }

        return responses;
    }

    /* =========================
       ORDER STATE
       ========================= */
    private void ensureTransitionAllowed(OrderStatus from, OrderStatus to) {
        if (from == OrderStatus.DELIVERED || from == OrderStatus.CANCELLED) {
            throw new RuntimeException("Final state cannot be changed");
        }
    }

    @Transactional
    public void markPaid(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        ensureTransitionAllowed(order.getStatus(), OrderStatus.PAID);
        order.setStatus(OrderStatus.PAID);
    }

    @Transactional
    public void markPaymentFailed(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        ensureTransitionAllowed(order.getStatus(), OrderStatus.PAYMENT_FAILED);
        order.setStatus(OrderStatus.PAYMENT_FAILED);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        ensureTransitionAllowed(order.getStatus(), OrderStatus.CANCELLED);
        order.setStatus(OrderStatus.CANCELLED);
    }

    public List<OrderItemResponse> getOrderItems(Long orderId) {
        return getOrderDetails(orderId).getItems();
    }
}

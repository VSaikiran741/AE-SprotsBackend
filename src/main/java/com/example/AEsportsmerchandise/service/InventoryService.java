//package com.example.AEsportsmerchandise.service;
//
//import com.example.AEsportsmerchandise.entity.OrderItemEntity;
//import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
//import com.example.AEsportsmerchandise.repository.OrderItemRepository;
//import com.example.AEsportsmerchandise.repository.ProductVariantRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class InventoryService {
//
//    private final ProductVariantRepository variantRepository;
//    private final OrderItemRepository orderItemRepository;
//
//    @Transactional
//    public void finalizeStock(Long orderId) {
//        var items = orderItemRepository.findByOrderIdWithVariant(orderId);
//
//        for (OrderItemEntity item : items) {
//            ProductVariantEntity v = item.getVariant();
//            v.setReservedStock(v.getReservedStock() - item.getQuantity());
//            v.setStock(v.getStock() - item.getQuantity());
//            variantRepository.save(v);
//        }
//    }
//
//    @Transactional
//    public void releaseReservedStock(Long orderId) {
//        var items = orderItemRepository.findByOrderIdWithVariant(orderId);
//
//        for (OrderItemEntity item : items) {
//            ProductVariantEntity v = item.getVariant();
//            v.setReservedStock(v.getReservedStock() - item.getQuantity());
//            variantRepository.save(v);
//        }
//    }
//}

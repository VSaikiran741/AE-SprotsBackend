package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductImageRequest;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ProductImageEntity;
import com.example.AEsportsmerchandise.repository.ProductImageRepository;
import com.example.AEsportsmerchandise.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class ProductImageController {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;

    @PostMapping("/{productId}/images")
    public void addImage(
            @PathVariable Long productId,
            @RequestBody ProductImageRequest request) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductImageEntity image = new ProductImageEntity();
        image.setProduct(product);
        image.setImageUrl(request.getImageUrl());
        image.setPublicId(request.getPublicId());
        image.setIsPrimary(Boolean.TRUE.equals(request.getIsPrimary()));
        image.setSortOrder(
                request.getSortOrder() != null ? request.getSortOrder() : 0
        );

        imageRepository.save(image);
    }
}

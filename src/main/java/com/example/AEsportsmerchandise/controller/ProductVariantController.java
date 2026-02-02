package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductVariantRequest;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
import com.example.AEsportsmerchandise.repository.ProductRepository;
import com.example.AEsportsmerchandise.repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class ProductVariantController {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    @PostMapping("/{productId}/variants")
    public void addVariant(
            @PathVariable Long productId,
            @RequestBody ProductVariantRequest request) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariantEntity variant = new ProductVariantEntity();
        variant.setProduct(product);
        variant.setSize(request.getSize());
        variant.setColor(request.getColor());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        variant.setReservedStock(0); // important
        variant.setActive(true);

        variantRepository.save(variant);
    }
}

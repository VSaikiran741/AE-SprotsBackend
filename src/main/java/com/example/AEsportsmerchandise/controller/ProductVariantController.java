package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductVariantRequest;
import com.example.AEsportsmerchandise.dto.ProductVariantResponse;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
import com.example.AEsportsmerchandise.repository.ProductRepository;
import com.example.AEsportsmerchandise.repository.ProductVariantRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class ProductVariantController {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    @PostMapping("/{productId}/variants")
    public ProductVariantResponse addVariant(
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
        variant.setReservedStock(0);
        variant.setActive(true);

        variant = variantRepository.save(variant);

        ProductVariantResponse dto = new ProductVariantResponse();
        dto.setId(variant.getId());
        dto.setSize(variant.getSize());
        dto.setColor(variant.getColor());
        dto.setSku(variant.getSku());
        dto.setPrice(variant.getPrice());
        dto.setStock(variant.getStock());
        dto.setActive(variant.getActive());

        // ✅ pricing always comes from ProductEntity
        dto.setBasePrice(product.getBasePrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setDiscountPercent(product.getDiscountPercent());

        return dto;
    }
}
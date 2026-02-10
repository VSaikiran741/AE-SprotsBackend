package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductImageRequest;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ProductImageEntity;
import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
import com.example.AEsportsmerchandise.repository.ProductImageRepository;
import com.example.AEsportsmerchandise.repository.ProductRepository;
import com.example.AEsportsmerchandise.repository.ProductVariantRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class ProductImageController {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final ProductVariantRepository variantRepository;

    @PostMapping("/{productId}/images")
    public void addImage(
            @PathVariable Long productId,
            @RequestBody ProductImageRequest request) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Validate Color + Size
        if (request.getColor() == null || request.getColor().trim().isEmpty()
                || request.getSize() == null || request.getSize().trim().isEmpty()) {
            throw new RuntimeException("Color and Size are required");
        }

        String color = request.getColor().trim();
        String size = request.getSize().trim();

        // ✅ Find Variant
        ProductVariantEntity variant = variantRepository
                .findByProduct_IdAndColorIgnoreCaseAndSizeIgnoreCase(productId, color, size);

        if (variant == null) {
            throw new RuntimeException("Variant not found for productId=" + productId +
                    ", color=" + color +
                    ", size=" + size);
        }

        // ✅ Save Image
        ProductImageEntity image = new ProductImageEntity();
        image.setProduct(product);
        image.setVariant(variant);

        image.setImageUrl(request.getImageUrl());
        image.setPublicId(request.getPublicId());
        image.setIsPrimary(Boolean.TRUE.equals(request.getIsPrimary()));
        image.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        // ✅ Save SKU directly into image table
        image.setSku(variant.getSku());

        imageRepository.save(image);
    }
}
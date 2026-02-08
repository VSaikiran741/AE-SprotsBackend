package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.*;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;

    // ================= CREATE PRODUCT =================
    @Transactional
    public ProductResponse create(ProductRequest request) {

        ProductEntity product = new ProductEntity();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setCountryCode(request.getCountryCode());

        product.setBasePrice(request.getBasePrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setReturnable(request.getReturnable());
        product.setReturnWindowDays(request.getReturnWindowDays());
        product.setActive(true);

        // ✅ Calculate Discount Percent
        if (request.getBasePrice() != null && request.getDiscountPrice() != null) {

            BigDecimal base = request.getBasePrice();
            BigDecimal discount = request.getDiscountPrice();

            if (base.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal percent = base.subtract(discount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(base, 0, RoundingMode.HALF_UP);

                product.setDiscountPercent(percent.intValue());
            }
        }

        // Save product first
        product = productRepository.save(product);

        // Save Variants
        if (request.getVariants() != null) {

            for (ProductVariantRequest v : request.getVariants()) {

                ProductVariantEntity variant = new ProductVariantEntity();
                variant.setProduct(product);
                variant.setSize(v.getSize());
                variant.setColor(v.getColor());
                variant.setPrice(v.getPrice());
                variant.setStock(v.getStock());
                variant.setReservedStock(0);
                variant.setActive(true);

                variantRepository.save(variant);
            }
        }

        // Save Images
        if (request.getImages() != null) {

            for (ProductImageRequest img : request.getImages()) {

                ProductImageEntity image = new ProductImageEntity();
                image.setProduct(product);
                image.setImageUrl(img.getImageUrl());
                image.setPublicId(img.getPublicId());

                image.setIsPrimary(img.getIsPrimary() != null ? img.getIsPrimary() : false);
                image.setSortOrder(img.getSortOrder() != null ? img.getSortOrder() : 0);

                imageRepository.save(image);
            }
        }

        return toProductResponse(product);
    }

    // ================= GET ALL PRODUCTS =================
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {

        List<ProductResponse> responses = new ArrayList<>();
        List<ProductEntity> products = productRepository.findAll();

        for (ProductEntity product : products) {
            responses.add(toProductResponse(product));
        }

        return responses;
    }

    // ================= GET PRODUCT BY ID =================
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toProductResponse(product);
    }

    // ================= GET PRODUCT BY SLUG =================
    @Transactional(readOnly = true)
    public ProductResponse getBySlug(String slug) {

        ProductEntity product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toProductResponse(product);
    }

    // ================= GET VARIANTS BY PRODUCT =================
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariants(Long productId) {

        List<ProductVariantEntity> variants =
                variantRepository.findByProduct_Id(productId);

        List<ProductVariantResponse> responses = new ArrayList<>();

        for (ProductVariantEntity v : variants) {
            responses.add(toVariantResponse(v));
        }

        return responses;
    }

    // ================= GET IMAGES BY PRODUCT =================
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImages(Long productId) {

        List<ProductImageEntity> images =
                imageRepository.findByProduct_Id(productId);

        List<ProductImageResponse> responses = new ArrayList<>();

        for (ProductImageEntity img : images) {
            responses.add(toImageResponse(img));
        }

        return responses;
    }

    // ================= ENTITY → PRODUCT RESPONSE DTO =================
    public ProductResponse toProductResponse(ProductEntity product) {

        ProductResponse dto = new ProductResponse();

        dto.setId(product.getId());
        dto.setSlug(product.getSlug());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());

        dto.setBasePrice(product.getBasePrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setDiscountPercent(product.getDiscountPercent());

        dto.setRating(BigDecimal.valueOf(product.getRating()));
        dto.setReviewCount(product.getReviewCount());

        dto.setActive(product.getActive());
        dto.setReturnable(product.getReturnable());
        dto.setReturnWindowDays(product.getReturnWindowDays());

        // Variants
        List<ProductVariantEntity> variantEntities =
                variantRepository.findByProduct_Id(product.getId());

        List<ProductVariantResponse> variantResponses = new ArrayList<>();
        for (ProductVariantEntity variant : variantEntities) {
            variantResponses.add(toVariantResponse(variant));
        }
        dto.setVariants(variantResponses);

        // Images
        List<ProductImageEntity> imageEntities =
                imageRepository.findByProduct_Id(product.getId());

        List<ProductImageResponse> imageResponses = new ArrayList<>();
        for (ProductImageEntity image : imageEntities) {
            imageResponses.add(toImageResponse(image));
        }
        dto.setImages(imageResponses);

        return dto;
    }

    // ================= VARIANT ENTITY → VARIANT RESPONSE DTO =================
    private ProductVariantResponse toVariantResponse(ProductVariantEntity v) {

        ProductVariantResponse dto = new ProductVariantResponse();

        dto.setId(v.getId());
        dto.setSize(v.getSize());
        dto.setColor(v.getColor());
        dto.setSku(v.getSku());
        dto.setPrice(v.getPrice());
        dto.setStock(v.getStock());
        dto.setActive(v.getActive());

        // ✅ Works because transaction session is active
        ProductEntity product = v.getProduct();
        dto.setBasePrice(product.getBasePrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setDiscountPercent(product.getDiscountPercent());

        return dto;
    }

    // ================= IMAGE ENTITY → IMAGE RESPONSE DTO =================
    private ProductImageResponse toImageResponse(ProductImageEntity img) {

        ProductImageResponse dto = new ProductImageResponse();

        dto.setId(img.getId());
        dto.setImageUrl(img.getImageUrl());
        dto.setPublicId(img.getPublicId());
        dto.setIsPrimary(img.getIsPrimary());
        dto.setSortOrder(img.getSortOrder());

        return dto;
    }

    // ================= UPDATE PRODUCT =================
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());

        product.setBasePrice(request.getBasePrice());
        product.setDiscountPrice(request.getDiscountPrice());

        product.setReturnable(request.getReturnable());
        product.setReturnWindowDays(request.getReturnWindowDays());

        // ✅ Recalculate Discount Percent
        if (request.getBasePrice() != null && request.getDiscountPrice() != null) {

            BigDecimal base = request.getBasePrice();
            BigDecimal discount = request.getDiscountPrice();

            if (base.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal percent = base.subtract(discount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(base, 0, RoundingMode.HALF_UP);

                product.setDiscountPercent(percent.intValue());
            }
        }

        productRepository.save(product);

        return toProductResponse(product);
    }

    // ================= SOFT DELETE PRODUCT =================
    @Transactional
    public void softDelete(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }
}
package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.*;
import com.example.AEsportsmerchandise.entity.*;
import com.example.AEsportsmerchandise.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        // 1. Create Product Entity
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

        // 2. Save product (ID generated here)
        product = productRepository.save(product);

        // 3. Save Variants (if present)
        if (request.getVariants() != null) {

            for (int i = 0; i < request.getVariants().size(); i++) {

                ProductVariantRequest v = request.getVariants().get(i);

                ProductVariantEntity variant = new ProductVariantEntity();
                variant.setProduct(product); // FK
                variant.setSize(v.getSize());
                variant.setColor(v.getColor());
                variant.setPrice(v.getPrice());
//                variant.setStock(v.getReservedStock());
                variant.setStock(v.getStock());
                variant.setReservedStock(0);
                variant.setActive(true);


                variantRepository.save(variant);
            }
        }

        // 4. Save Images (if present)
        if (request.getImages() != null) {

            for (int i = 0; i < request.getImages().size(); i++) {

                ProductImageRequest img = request.getImages().get(i);

                ProductImageEntity image = new ProductImageEntity();
                image.setProduct(product); // FK
                image.setImageUrl(img.getImageUrl());
                image.setPublicId(img.getPublicId());

                if (img.getIsPrimary() != null) {
                    image.setIsPrimary(img.getIsPrimary());
                } else {
                    image.setIsPrimary(false);
                }

                if (img.getSortOrder() != null) {
                    image.setSortOrder(img.getSortOrder());
                } else {
                    image.setSortOrder(0);
                }

                imageRepository.save(image);
            }
        }

        // 5. Return response DTO
        return toProductResponse(product);
    }

    // ================= GET ALL PRODUCTS =================
    public List<ProductResponse> getAll() {

        List<ProductResponse> responses = new ArrayList<>();

        List<ProductEntity> products = productRepository.findAll();

        for (int i = 0; i < products.size(); i++) {
            ProductEntity product = products.get(i);
            responses.add(toProductResponse(product));
        }

        return responses;
    }

    // ================= GET PRODUCT BY ID =================
    public ProductResponse getById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toProductResponse(product);
    }

    // ================= GET PRODUCT BY SLUG =================
    public ProductResponse getBySlug(String slug) {

        ProductEntity product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toProductResponse(product);
    }

    // ================= ENTITY → RESPONSE DTO =================
    public ProductResponse toProductResponse(ProductEntity product) {

        ProductResponse dto = new ProductResponse();

        dto.setId(product.getId());
        dto.setSlug(product.getSlug());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
//        dto.setCountryCode(product.getCountryCode());


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

        for (int i = 0; i < variantEntities.size(); i++) {
            variantResponses.add(toVariantResponse(variantEntities.get(i)));
        }

        dto.setVariants(variantResponses);

        // Images
        List<ProductImageEntity> imageEntities =
                imageRepository.findByProduct_Id(product.getId());


        List<ProductImageResponse> imageResponses = new ArrayList<>();

        for (int i = 0; i < imageEntities.size(); i++) {
            imageResponses.add(toImageResponse(imageEntities.get(i)));
        }

        dto.setImages(imageResponses);

        return dto;
    }
    // ================= GET VARIANTS BY PRODUCT =================
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
    public List<ProductImageResponse> getImages(Long productId) {

        List<ProductImageEntity> images =
                imageRepository.findByProduct_Id(productId);

        List<ProductImageResponse> responses = new ArrayList<>();

        for (ProductImageEntity img : images) {
            responses.add(toImageResponse(img));
        }

        return responses;
    }


    // ================= VARIANT ENTITY → DTO =================
    private ProductVariantResponse toVariantResponse(ProductVariantEntity v) {

        ProductVariantResponse dto = new ProductVariantResponse();
        dto.setId(v.getId());
        dto.setSize(v.getSize());
        dto.setColor(v.getColor());
        dto.setSku(v.getSku());
        dto.setPrice(v.getPrice());
        dto.setStock(v.getStock());
        dto.setActive(v.getActive());

        // ✅ product pricing inside variant response
        ProductEntity p = v.getProduct();
        dto.setBasePrice(p.getBasePrice());
        dto.setDiscountPrice(p.getDiscountPrice());
        dto.setDiscountPercent(p.getDiscountPercent());

        return dto;
    }
    // ================= IMAGE ENTITY → DTO =================
    private ProductImageResponse toImageResponse(ProductImageEntity img) {

        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(img.getId());
        dto.setImageUrl(img.getImageUrl());
        dto.setPublicId(img.getPublicId());
        dto.setIsPrimary(img.getIsPrimary());
        dto.setSortOrder(img.getSortOrder());

        return dto;
    }
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

        productRepository.save(product);

        return toProductResponse(product);
    }

    // ================= SOFT DELETE PRODUCT =================
    public void softDelete(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }
}

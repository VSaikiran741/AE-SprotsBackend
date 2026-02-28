package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductResponse;
import com.example.AEsportsmerchandise.dto.ProductVariantResponse;
import com.example.AEsportsmerchandise.dto.ProductImageResponse;
import com.example.AEsportsmerchandise.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(
        name = "Products",
        description = "Public product browsing APIs"
)
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @Operation(
            summary = "Get suggested products",
            description = "Fetch related products from same category"
    )
    @GetMapping("/{id}/suggested")
    public List<ProductResponse> getSuggestedProducts(@PathVariable Long id) {
        return productService.getSuggestedProducts(id);
    }

    @Operation(
            summary = "Get all products",
            description = "Fetches all available products for public browsing"
    )
    @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @Operation(
            summary = "Get product by ID",
            description = "Fetches a single product using its unique ID"
    )
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(
            summary = "Get product variants",
            description = "Fetches all variants (size, color, etc.) of a product"
    )
    @ApiResponse(responseCode = "200", description = "Variants fetched successfully")
    @GetMapping("/{id}/variants")
    public List<ProductVariantResponse> getVariants(@PathVariable Long id) {
        return productService.getVariants(id);
    }

    @Operation(
            summary = "Get product images",
            description = "Fetches all images associated with a product"
    )
    @ApiResponse(responseCode = "200", description = "Images fetched successfully")
    @GetMapping("/{id}/images")
    public List<ProductImageResponse> getImages(@PathVariable Long id) {
        return productService.getImages(id);
    }
    @Operation(
            summary = "Search and filter products",
            description = "Search products by keyword, category, and price"
    )
    @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double price
    ) {
        return productService.searchProducts(q, category, BigDecimal.valueOf(price));
    }

}

package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductRequest;
import com.example.AEsportsmerchandise.dto.ProductResponse;
import com.example.AEsportsmerchandise.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Admin - Products",
        description = "Admin product management APIs"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    @Operation(
            summary = "Create product",
            description = "Creates a new product (Admin only)"
    )
    @ApiResponse(responseCode = "200", description = "Product created successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PostMapping
    public ProductResponse create(
            @RequestBody ProductRequest request
    ) {
        return productService.create(request);
    }

    @Operation(
            summary = "Update product",
            description = "Updates an existing product by ID (Admin only)"
    )
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        return productService.update(id, request);
    }

    @Operation(
            summary = "Get all products (Admin)",
            description = "Returns all products including inactive or soft-deleted ones"
    )
    @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    @GetMapping
    public List<ProductResponse> getAllForAdmin() {
        return productService.getAll();
    }

    @Operation(
            summary = "Soft delete product",
            description = "Soft deletes a product so it is hidden from users but retained in the database"
    )
    @ApiResponse(responseCode = "204", description = "Product soft deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public void softDelete(
            @PathVariable Long id
    ) {
        productService.softDelete(id);
    }
}

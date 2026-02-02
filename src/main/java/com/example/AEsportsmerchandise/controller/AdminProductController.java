package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ProductRequest;
import com.example.AEsportsmerchandise.dto.ProductResponse;
import com.example.AEsportsmerchandise.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }
    @GetMapping
    public List<ProductResponse> getAllForAdmin() {
        return productService.getAll();
    }


    @DeleteMapping("/{id}")
    public void softDelete(@PathVariable Long id) {
        productService.softDelete(id);
    }
}

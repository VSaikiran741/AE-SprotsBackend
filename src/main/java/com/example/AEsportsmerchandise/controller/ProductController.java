    package com.example.AEsportsmerchandise.controller;

    import com.example.AEsportsmerchandise.dto.*;
    import com.example.AEsportsmerchandise.service.ProductService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    @RestController
    @RequestMapping("/products")
    @RequiredArgsConstructor
    public class ProductController {

        private final ProductService productService;

        @GetMapping
        public List<ProductResponse> getAll() {
            return productService.getAll();
        }

        @GetMapping("/{id}")
        public ProductResponse getById(@PathVariable Long id) {
            return productService.getById(id);
        }

        @GetMapping("/{id}/variants")
        public List<ProductVariantResponse> getVariants(@PathVariable Long id) {
            return productService.getVariants(id);
        }

        @GetMapping("/{id}/images")
        public List<ProductImageResponse> getImages(@PathVariable Long id) {
            return productService.getImages(id);
        }
    }

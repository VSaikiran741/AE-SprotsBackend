package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ReviewRequest;
import com.example.AEsportsmerchandise.dto.ReviewResponse;
import com.example.AEsportsmerchandise.entity.ReviewEntity;
import com.example.AEsportsmerchandise.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getProductReviews(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void addReview(@RequestBody ReviewRequest request) {
        reviewService.addReview(request);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
    }


}

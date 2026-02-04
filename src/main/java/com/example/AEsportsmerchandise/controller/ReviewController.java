package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.ReviewRequest;
import com.example.AEsportsmerchandise.dto.ReviewResponse;
import com.example.AEsportsmerchandise.service.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Reviews",
        description = "Product reviews APIs"
)
//@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Get product reviews",
            description = "Returns all reviews for a specific product"
    )
    @ApiResponse(responseCode = "200", description = "Reviews fetched successfully")
    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getProductReviews(
            @PathVariable Long productId
    ) {
        return reviewService.getReviewsByProduct(productId);
    }

    @Operation(
            summary = "Add product review",
            description = "Allows an authenticated user to add a review for a product"
    )
    @ApiResponse(responseCode = "200", description = "Review added successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")

    public void addReview(
            @RequestBody ReviewRequest request
    ) {
        reviewService.addReview(request);
    }

    @Operation(
            summary = "Delete review (Admin)",
            description = "Allows an admin to delete an inappropriate or invalid review"
    )
    @ApiResponse(responseCode = "204", description = "Review deleted successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")

    public void deleteReview(
            @PathVariable Long id
    ) {
        reviewService.delete(id);
    }
}

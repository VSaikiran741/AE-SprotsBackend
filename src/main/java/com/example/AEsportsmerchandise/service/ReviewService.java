package com.example.AEsportsmerchandise.service;

import com.example.AEsportsmerchandise.dto.ReviewRequest;
import com.example.AEsportsmerchandise.dto.ReviewResponse;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ReviewEntity;
import com.example.AEsportsmerchandise.entity.UserEntity;
import com.example.AEsportsmerchandise.repository.OrderRepository;
import com.example.AEsportsmerchandise.repository.ProductRepository;
import com.example.AEsportsmerchandise.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // ✅ ADD REVIEW (logged-in user)
    @Transactional
    public void addReview(ReviewRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();

        System.out.println("Logged in user id = " + user.getId());

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Only buyers can review
        boolean hasPurchased =
                orderRepository.existsByUserIdAndProductId(user.getId(), product.getId());

        if (!hasPurchased) {
            throw new RuntimeException("Only purchased users can review this product");
        }

        // ✅ Prevent duplicate reviews
        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        // Save review
        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setVerifiedPurchase(true);

        reviewRepository.save(review);

        // ✅ Update product rating
        double oldAvg = product.getRating() == 0.0 ? 0.0 : product.getRating();
        int total = product.getReviewCount() == null ? 0 : product.getReviewCount();

        double newAvg = (oldAvg * total + request.getRating()) / (total + 1);

        product.setRating(newAvg);
        product.setReviewCount(total + 1);

        productRepository.save(product);
    }
    public List<ReviewResponse> getReviewsByVerifiedPurchase(boolean verified) {

        return reviewRepository.findByVerifiedPurchase(verified)
                .stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getUser().getEmail(),
                        review.getVerifiedPurchase(),
                        review.getCreatedAt()
                ))
                .toList();
    }



    // ✅ GET REVIEWS BY PRODUCT (public)
    public List<ReviewResponse> getReviewsByProduct(Long productId) {

        return reviewRepository.findByProductId(productId)
                .stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getUser().getEmail(),   // ✅ SAFE NOW
                        review.getVerifiedPurchase(),
                        review.getCreatedAt()
                ))
                .toList();
    }


    // ✅ DELETE REVIEW (optional, usually admin)
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}

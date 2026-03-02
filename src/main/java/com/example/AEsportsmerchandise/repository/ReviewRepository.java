package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.dto.ReviewResponse;
import com.example.AEsportsmerchandise.entity.ProductEntity;
import com.example.AEsportsmerchandise.entity.ReviewEntity;
//import com.example.AEsportsmerchandise.entity.ReviewStatus;
import com.example.AEsportsmerchandise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("""
SELECT new com.example.AEsportsmerchandise.dto.ReviewResponse(
    r.id, r.rating, r.comment, u.email, r.verifiedPurchase, r.createdAt
)
FROM ReviewEntity r
JOIN r.user u
WHERE r.product.id = :productId
""")
    List<ReviewResponse> findReviewsByProduct(Long productId);

    List<ReviewEntity> findByProductId(Long productId);
    boolean existsByUserAndProduct(UserEntity user, ProductEntity product);
//    List<ReviewEntity> findByStatus(ReviewStatus status);
    List<ReviewEntity> findByVerifiedPurchase(Boolean verifiedPurchase);


}

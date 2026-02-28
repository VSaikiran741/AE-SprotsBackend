package com.example.AEsportsmerchandise.entity;

import com.example.AEsportsmerchandise.dto.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "user_id"})
)
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    private Integer rating;
    private String comment;
    private Boolean verifiedPurchase = true;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING; // ⭐

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findBySlug(String slug);
    @Query("""
    SELECT p FROM ProductEntity p
    WHERE p.active = true
      AND (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')))
      AND (:category IS NULL OR p.category = :category)
      AND (:price IS NULL OR p.basePrice = :price)
""")
    List<ProductEntity> searchProducts(
            @Param("q") String q,
            @Param("category") String category,
            @Param("price") BigDecimal price
    );
    @Query("""
    SELECT p FROM ProductEntity p
    WHERE p.active = true
      AND p.category = :category
      AND p.id <> :productId
""")
    List<ProductEntity> findSuggestedProducts(
            @Param("category") String category,
            @Param("productId") Long productId
    );

}



package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImageEntity, Long> {

    List<ProductImageEntity> findByProduct_Id(Long productId);
    @Query("SELECT pi FROM ProductImageEntity pi LEFT JOIN FETCH pi.variant WHERE pi.product.id = :productId")
    List<ProductImageEntity> findByProductIdWithVariant(@Param("productId") Long productId);
}


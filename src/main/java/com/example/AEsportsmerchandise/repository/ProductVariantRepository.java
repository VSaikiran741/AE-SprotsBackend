package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {

    List<ProductVariantEntity> findByProduct_Id(Long productId);

    ProductVariantEntity findByProduct_IdAndColorIgnoreCaseAndSizeIgnoreCase(Long productId, String color, String size);
}
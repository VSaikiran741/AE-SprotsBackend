package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariantEntity, Long> {

    List<ProductVariantEntity> findByProduct_Id(Long productId);

    List<ProductVariantEntity> findByProductId(Long id);
}

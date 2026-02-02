package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImageEntity, Long> {

    List<ProductImageEntity> findByProduct_Id(Long productId);
}


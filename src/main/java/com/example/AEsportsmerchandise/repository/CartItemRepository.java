package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByCartIdAndVariantId(Long cartId, Long variantId);
}

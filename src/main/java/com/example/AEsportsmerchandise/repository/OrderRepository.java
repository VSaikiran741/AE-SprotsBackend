package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.OrderEntity;
import com.example.AEsportsmerchandise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
        SELECT COUNT(o) > 0
        FROM OrderEntity o
        JOIN o.items i
        JOIN i.variant v
        JOIN v.product p
        WHERE o.user.id = :userId
          AND p.id = :productId
          AND o.status IN (
              com.example.AEsportsmerchandise.entity.OrderStatus.PAID,
              com.example.AEsportsmerchandise.entity.OrderStatus.DELIVERED
          )
    """)
    boolean existsByUserIdAndProductId(
            @Param("userId") Long userId,
            @Param("productId") Long productId
    );

    List<OrderEntity> findByUser(UserEntity user);

    Optional<OrderEntity> findByIdAndUser(Long id, UserEntity user);
}

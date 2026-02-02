package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query("""
        select oi
        from OrderItemEntity oi
        join fetch oi.variant v
        join fetch v.product
        where oi.order.id = :orderId
    """)
    List<OrderItemEntity> findByOrderIdWithVariant(@Param("orderId") Long orderId);
}


//package com.example.AEsportsmerchandise.repository;
//
//import com.example.AEsportsmerchandise.dto.TrendPointDTO;
//import com.example.AEsportsmerchandise.entity.OrderEntity;
//import com.example.AEsportsmerchandise.entity.OrderStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.Repository;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public interface AdminDashboardRepository
//        extends JpaRepository<OrderEntity, Long> {
//
//    @Query("SELECT COUNT(o) FROM OrderEntity o")
//    long countTotalOrders();
//
//    @Query("""
//        SELECT COUNT(o)
//        FROM OrderEntity o
//        WHERE FUNCTION('DATE', o.createdAt) = CURRENT_DATE
//    """)
//    long countOrdersToday();
//
//    @Query("""
//        SELECT COALESCE(SUM(o.totalAmount), 0)
//        FROM OrderEntity o
//        WHERE FUNCTION('DATE', o.createdAt) = CURRENT_DATE
//          AND o.status = 'PAID'
//    """)
//    BigDecimal sumRevenueToday();
//
//    @Query("""
//        SELECT COUNT(o)
//        FROM OrderEntity o
//        WHERE o.status = 'PAYMENT_FAILED'
//    """)
//    long countFailedPayments();
//
//    @Query("""
//        SELECT COUNT(o)
//        FROM OrderEntity o
//        WHERE o.status = :status
//    """)
//    long countByOrderStatus(OrderStatus status);
//
//    @Query("SELECT COUNT(u) FROM UserEntity u")
//    long countUsers();
//
//    @Query("SELECT COUNT(p) FROM ProductEntity p")
//    long countProducts();
//
//    @Query("""
//        SELECT new com.example.AEsportsmerchandise.dto.TrendPointDTO(
//            FUNCTION('DATE', o.createdAt),
//            COUNT(o),
//            COALESCE(SUM(o.totalAmount), 0)
//        )
//        FROM OrderEntity o
//        WHERE o.createdAt >= CURRENT_DATE - 7
//        GROUP BY FUNCTION('DATE', o.createdAt)
//        ORDER BY FUNCTION('DATE', o.createdAt)
//    """)
//    List<TrendPointDTO> orderTrendsLast7Days();
//}

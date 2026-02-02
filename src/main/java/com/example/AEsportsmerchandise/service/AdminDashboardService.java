//package com.example.AEsportsmerchandise.service;
//
//import com.example.AEsportsmerchandise.dto.DashboardSummaryResponse;
//import com.example.AEsportsmerchandise.dto.TrendPointDTO;
//import com.example.AEsportsmerchandise.entity.OrderStatus;
//import com.example.AEsportsmerchandise.repository.AdminDashboardRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
////@RequiredArgsConstructor
//
//public class AdminDashboardService {
//
//    private final AdminDashboardRepository r;
//
//    public DashboardSummaryResponse getSummary() {
//
//        DashboardSummaryResponse res = new DashboardSummaryResponse();
//
//        res.setTotalOrders(r.countTotalOrders());
//        res.setOrdersToday(r.countOrdersToday());
//        res.setRevenueToday(r.sumRevenueToday());
//        res.setPendingShipments(r.countByOrderStatus(OrderStatus.PAID));
//        res.setDeliveredOrders(r.countByOrderStatus(OrderStatus.DELIVERED));
//        res.setFailedPayments(r.countFailedPayments());
//        res.setTotalUsers(r.countUsers());
//        res.setTotalProducts(r.countProducts());
//
//        return res;
//    }
//
//    public List<TrendPointDTO> getOrderTrends() {
//        return r.orderTrendsLast7Days();
//    }
//}
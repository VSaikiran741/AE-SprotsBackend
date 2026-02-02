//package com.example.AEsportsmerchandise.controller;
//
//import com.example.AEsportsmerchandise.dto.DashboardSummaryResponse;
//import com.example.AEsportsmerchandise.dto.TrendPointDTO;
//import com.example.AEsportsmerchandise.service.AdminDashboardService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//@RestController
//@RequestMapping("/api/admin/dashboard")
//@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//public class AdminDashboardController {
//
//    private final AdminDashboardService dashboardService;
//
//    @GetMapping("/summary")
//    public DashboardSummaryResponse summary() {
//        return dashboardService.getSummary();
//    }
//
//    @GetMapping("/trends")
//    public List<TrendPointDTO> trends() {
//        return dashboardService.getOrderTrends();
//    }
//}
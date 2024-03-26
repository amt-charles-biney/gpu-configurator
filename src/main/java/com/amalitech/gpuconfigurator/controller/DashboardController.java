package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;

import com.amalitech.gpuconfigurator.dto.DeliveryGoalDto;
import com.amalitech.gpuconfigurator.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @CrossOrigin
    @GetMapping("/v1/admin/dashboard")
    public ResponseEntity<DashboardInfoDto> getSTats() {

        return ResponseEntity.ok(dashboardService.dashboardStat());
    }

    @CrossOrigin
    @GetMapping("/v1/admin/dashboard/revenue")
    public ResponseEntity<Map<DayOfWeek, BigDecimal>> getRevenue(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {

        return ResponseEntity.ok(dashboardService.revenueStat(startDate, endDate));
    }

    @CrossOrigin
    @GetMapping("/v1/admin/dashboard/goals")
    public ResponseEntity<DeliveryGoalDto> getDeliveryGoalStats(
            @RequestParam String month
    ) {
        return ResponseEntity.ok(dashboardService.deliveryStat(month));
    }

}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;

import com.amalitech.gpuconfigurator.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


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
    public ResponseEntity<DashboardInfoDto> getRevenue(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {

        return ResponseEntity.ok(null);
    }

}

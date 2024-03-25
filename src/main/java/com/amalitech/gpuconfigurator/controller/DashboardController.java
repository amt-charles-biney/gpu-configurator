package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;

import com.amalitech.gpuconfigurator.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @CrossOrigin
    @GetMapping("/v1/admin/dashboard")
    public ResponseEntity<DashboardInfoDto> getAllBrands() {

        return ResponseEntity.ok(dashboardService.dashboardStat());
    }
}

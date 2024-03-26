package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
import com.amalitech.gpuconfigurator.dto.DeliveryGoalDto;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;


public interface DashboardService {
    DashboardInfoDto dashboardStat();

    Map<DayOfWeek, BigDecimal> revenueStat(LocalDate startDate, LocalDate endDate);

    DeliveryGoalDto deliveryStat(String month);
}

package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
import com.amalitech.gpuconfigurator.dto.DeliveryGoalDto;
import com.amalitech.gpuconfigurator.dto.RevenueDto;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;


public interface DashboardService {
    DashboardInfoDto dashboardStat();

    RevenueDto revenueStat(LocalDate startDate, LocalDate endDate);

    DeliveryGoalDto deliveryStat(String month);
}

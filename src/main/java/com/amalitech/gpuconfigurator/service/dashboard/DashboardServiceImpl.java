package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
import com.amalitech.gpuconfigurator.dto.DeliveryGoalDto;
import com.amalitech.gpuconfigurator.dto.RevenueDto;
import com.amalitech.gpuconfigurator.dto.order.LatestOrderDto;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public DashboardInfoDto dashboardStat() {
        Long customers = orderRepository.customers();
        Long orders = orderRepository.orders();
        Long products = productRepository.productsTotal();
        BigDecimal revenue = paymentRepository.revenue();

        return DashboardInfoDto.builder()
                .customers(customers)
                .orders(orders)
                .products(products)
                .revenue(revenue != null ? revenue.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .latestOrders(latestOrder())
                .build();
    }

    @Override
    public RevenueDto revenueStat(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minus(6, ChronoUnit.DAYS);
        }

        List<Object[]> revenueData = paymentRepository.revenueRange(startDate, endDate);
        Map<DayOfWeek, BigDecimal> revenueByDayOfWeek = new EnumMap<>(DayOfWeek.class);

        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();

        for (Object[] row : revenueData) {
            LocalDate date = (LocalDate) row[0];
            BigDecimal revenue = (BigDecimal) row[1];
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            revenueByDayOfWeek.put(dayOfWeek, revenueByDayOfWeek.getOrDefault(dayOfWeek, BigDecimal.ZERO).add(revenue));
        }

        DayOfWeek currentDay = startDate.getDayOfWeek();
        for (int i = 0; i < DayOfWeek.values().length; i++) {
            BigDecimal dayRevenue = revenueByDayOfWeek.getOrDefault(currentDay, BigDecimal.ZERO);
            dayOfWeeks.add(currentDay);
            revenues.add(dayRevenue);
            currentDay = currentDay.plus(1);
        }

        return RevenueDto.builder()
                .dayOfWeeks(dayOfWeeks)
                .revenue(revenues)
                .build();
    }


    @Override
    public DeliveryGoalDto deliveryStat(String month) {
        Month convertMonth;
        try {
            convertMonth = Month.valueOf(month.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        int monthValue = convertMonth.getValue();
        int currentYear = Year.now().getValue();

        Long deliveryStatusCount = orderRepository.deliveredStatusCount(monthValue, currentYear);

        int target = 100; // this will change once we decided on how an admin will set a target

        float percentage = (float) (deliveryStatusCount * 100.0 / target);
        percentage = Math.round(percentage * 100) / 100f;

        return DeliveryGoalDto.builder()
                .percentage(percentage)
                .totalDeliveredItems(deliveryStatusCount)
                .build();
    }


    private List<LatestOrderDto> latestOrder() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("createdAt").descending());

        List<Order> orders = orderRepository.findAll(pageable).getContent();

        return orders.stream().map(this::mapToLatestOrder).toList();
    }

    private LatestOrderDto mapToLatestOrder(Order order) {
        return LatestOrderDto.builder()
                .productName(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductName()).orElse(null))
                .coverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                .orderedTime(order.getCreatedAt())
                .status(order.getStatus())
                .build();
    }
}

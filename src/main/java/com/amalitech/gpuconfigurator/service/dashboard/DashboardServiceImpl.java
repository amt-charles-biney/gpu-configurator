package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
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
                .revenue(revenue.setScale(2, RoundingMode.HALF_UP))
                .latestOrders(latestOrder())
                .build();
    }

    @Override
    public Map<DayOfWeek, BigDecimal> revenueStat(LocalDate startDate, LocalDate endDate) {
        List<Object[]> revenueData = paymentRepository.revenueRange(startDate, endDate);
        Map<DayOfWeek, BigDecimal> revenueByDayOfWeek = new EnumMap<>(DayOfWeek.class);

        for (Object[] row : revenueData) {
            LocalDate date = (LocalDate) row[0];
            BigDecimal revenue = (BigDecimal) row[1];
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            revenueByDayOfWeek.put(dayOfWeek, revenueByDayOfWeek.getOrDefault(dayOfWeek, BigDecimal.ZERO.add(revenue)));
        }
        return revenueByDayOfWeek;
    }


    private List<LatestOrderDto> latestOrder() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("createdAt").descending());

        List<Order> orders = orderRepository.findAll(pageable).getContent();

        return orders.stream().map(this::mapToLatestOrder).toList();
    }

    private LatestOrderDto mapToLatestOrder(Order order) {
        return LatestOrderDto.builder()
                .coverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                .orderedTime(order.getCreatedAt())
                .status(order.getStatus())
                .build();
    }
}

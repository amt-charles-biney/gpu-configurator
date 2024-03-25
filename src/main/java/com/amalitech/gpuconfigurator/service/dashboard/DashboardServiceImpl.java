package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
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
import java.util.List;

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
                .build();
    }
}

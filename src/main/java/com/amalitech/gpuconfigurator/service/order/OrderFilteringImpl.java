package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFilteringImpl implements OrderFiltering {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderResponseDto> orders(String status) {
        Specification<Order> spec = createOrderSpecification(status);
        List<Order> orders = orderRepository.findAll(spec);
        return orders.stream().map(
                order -> OrderResponseDto.builder()
                        .orderId(order.getId())
                        .configuredProduct(order.getCart().getConfiguredProducts())
                        .productCoverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                                .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                        .paymentMethod(order.getPayment().getChannel())
                        .productName(order.getCart().getConfiguredProducts().stream().findFirst()
                                .map(prod -> prod.getProduct().getProductName()).orElse(null))
                        .paymentMethod(order.getPayment().getChannel())
                        .status(order.getStatus())
                        .customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                        .totalPrice(order.getCart().getConfiguredProducts().stream()
                                .map(Configuration::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .date(order.getCreatedAt())
                        .build()
        ).toList();
    }

    private Specification<Order> createOrderSpecification(String status) {
        Specification<Order> spec = Specification.where(null);
        spec = applyStatusFilter(spec, status);
        return spec;
    }

    private Specification<Order> applyStatusFilter(Specification<Order> spec, String status) {
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        return spec;
    }
}
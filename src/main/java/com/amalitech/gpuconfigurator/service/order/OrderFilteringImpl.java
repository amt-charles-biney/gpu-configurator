package com.amalitech.gpuconfigurator.service.order;

import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderFilteringImpl implements OrderFiltering {

    private final OrderRepository orderRepository;

    @Override
    public Page<OrderResponseDto> orders(String status, LocalDate startDate, LocalDate endDate, Integer page, Integer size) {
        Specification<Order> spec = createOrderSpecification(status, startDate, endDate);
        Pageable pageable = getPageable(page, size);
        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);
        List<OrderResponseDto> orderResponseDtos = ordersPage.getContent().stream()
                .map(this::mapOrderToOrderResponseDto)
                .toList();
        return new PageImpl<>(orderResponseDtos, ordersPage.getPageable(), ordersPage.getTotalElements());
    }


    @Override

    public Page<OrderResponseDto> ordersUser(String status, LocalDate startDate, LocalDate endDate, Integer page, Integer size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Specification<Order> userSpec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
        Specification<Order> spec = createOrderSpecification(status, startDate, endDate);
        Specification<Order> combinedSpec = userSpec.and(spec);

        Pageable pageable = getPageable(page, size);
        Page<Order> ordersPage = orderRepository.findAll(combinedSpec, pageable);

        List<OrderResponseDto> orderResponseDtos = ordersPage.getContent().stream()
                .map(this::mapOrderToOrderResponseDto)
                .toList();

        return new PageImpl<>(orderResponseDtos, ordersPage.getPageable(), ordersPage.getTotalElements());
    }

    private Specification<Order> createOrderSpecification(String status, LocalDate startDate, LocalDate endDate) {
        Specification<Order> spec = Specification.where(null);
        spec = applyStatusFilter(spec, status);
        spec = createOrderDateRangeSpecification(spec, startDate, endDate);
        return spec;
    }

    private Specification<Order> createOrderDateRangeSpecification(Specification<Order> spec, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), startDate, endDate)));

        } else if (startDate != null) {
            spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate)));
        }
        return spec;
    }

    private Specification<Order> applyStatusFilter(Specification<Order> spec, String status) {
        if (status != null && !"all".equalsIgnoreCase(status) && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        return spec;
    }

    @NotNull
    private static Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(page, size, Sort.by("createdAt").ascending());
    }

    private OrderResponseDto mapOrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderId(order.getTrackingId())
                .configuredProduct(order.getCart().getConfiguredProducts())
                .productCoverImage(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductCase().getCoverImageUrl()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .productName(order.getCart().getConfiguredProducts().stream().findFirst()
                        .map(prod -> prod.getProduct().getProductName()).orElse(null))
                .paymentMethod(order.getPayment().getChannel())
                .status(order.getStatus())
                .trackingUrl(order.getTrackingUrl())
                .customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .totalPrice(order.getCart().getConfiguredProducts().stream()
                        .map(Configuration::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .date(order.getCreatedAt())
                .build();
    }
}

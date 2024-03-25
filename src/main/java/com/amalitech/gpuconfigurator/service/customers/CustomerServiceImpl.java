package com.amalitech.gpuconfigurator.service.customers;

import com.amalitech.gpuconfigurator.dto.customers.CustomerResponseDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final OrderRepository orderRepository;

    @Override
    public Page<CustomerResponseDto> getAllCustomers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.selectAllUsersWithOrderCount(pageable).map(this::mapToCustomerResponse);
    }


    private CustomerResponseDto mapToCustomerResponse(Object[] result) {
        User user = (User) result[0];
        Long numberOfOrders = (Long) result[1];
        BigDecimal totalAmtByCustomer = (BigDecimal) result[2];
        LocalDate dateJoined = (LocalDate) result[3];

        return CustomerResponseDto.builder()
                .name(user.getFirstName() + " " + user.getLastName())
                .numberOfOrders(numberOfOrders.intValue())
                .amountSpent(totalAmtByCustomer.setScale(2, RoundingMode.HALF_UP))
                .joinDate(dateJoined)
                .build();
    }
}

package com.amalitech.gpuconfigurator.service.customers;

import com.amalitech.gpuconfigurator.dto.customers.CustomerResponseDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final OrderRepository orderRepository;

    @Override
    public Page<CustomerResponseDto> getAllCustomers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.selectAllUsers(pageable).map(this::mapToCustomerResponse);
    }

    private CustomerResponseDto mapToCustomerResponse(User user) {
        return CustomerResponseDto.builder()
                .name(user.getFirstName() + " " + user.getLastName())
                .build();
    }
}

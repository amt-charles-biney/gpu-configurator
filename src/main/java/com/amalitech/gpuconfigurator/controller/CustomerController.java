package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.customers.CustomerResponseDto;
import com.amalitech.gpuconfigurator.service.customers.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Get all customers",
            method = "GET"
    )
    @GetMapping("/v1/admin/customers")
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size
    ) {
        Page<CustomerResponseDto> resultPage = customerService.getAllCustomers(page, size);
        return ResponseEntity.ok(resultPage);
    }

}

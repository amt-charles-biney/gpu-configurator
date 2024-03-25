package com.amalitech.gpuconfigurator.service.dashboard;

import com.amalitech.gpuconfigurator.dto.DashboardInfoDto;
import com.amalitech.gpuconfigurator.repository.OrderRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void givenDetailsObject_whenGettingDashboardInfo_thenReturnWithDashboardInfo() {
        //give - precondition or setup

        // when - action or the behaviour under test
        when(orderRepository.customers()).thenReturn(1L);
        when(orderRepository.orders()).thenReturn(1L);
        when(productRepository.productsTotal()).thenReturn(1L);
        when(paymentRepository.revenue()).thenReturn(BigDecimal.TEN);
        DashboardInfoDto result = dashboardService.dashboardStat();

        //then - verify the output
        assertNotNull(result);
        assertEquals(1L, result.customers());
        assertEquals(1L, result.orders());
        assertEquals(1L, result.customers());
        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP), result.revenue());
    }
}
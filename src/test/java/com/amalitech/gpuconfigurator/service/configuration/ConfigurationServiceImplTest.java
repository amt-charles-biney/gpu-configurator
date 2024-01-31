package com.amalitech.gpuconfigurator.service.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ConfigurationServiceImplTest {

    @InjectMocks
    private ConfigurationService configurationService;

    @Test
    void configuration() {

        String productId = "pf91c34b-1705-4afd-8a1e-99d5fa21c249";
        Boolean warranty = false;

    }

    @Test
    void saveConfiguration() {
    }

    @Test
    void getSpecificConfiguration() {
    }

    @Test
    void deleteSpecificConfiguration() {
    }
}
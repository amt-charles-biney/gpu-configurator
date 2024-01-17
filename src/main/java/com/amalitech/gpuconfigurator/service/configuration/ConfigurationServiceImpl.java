package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationRequestDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryConfig.CategoryConfigService;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final CompatibleOptionService compatibleOptionService;
    private final ProductRepository productRepository;
    private final CategoryConfigService categoryConfigService;
    private final CategoryConfigRepository categoryConfigRepository;
    private final ConfigurationRepository configurationRepository;


    @Override
    public ConfigurationResponseDto createConfiguration(ConfigurationRequestDto request) {
        Product product = productRepository.findById(UUID.fromString(request.productId()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        BigDecimal totalPrice = BigDecimal.valueOf(product.getProductPrice());

        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(request.categoryId()))
                .orElseThrow(() -> new EntityNotFoundException("Configuration does not exist for the specified category"));

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        List<ConfigOptions> configOptions = compatibleOptions.stream()
                .filter(CompatibleOption::getIsIncluded)
                .map(option -> ConfigOptions.builder()
                        .optionId(String.valueOf(option.getId()))
                        .optionName(option.getName())
                        .optionPrice(option.getPrice())
                        .optionType(option.getType())
                        .build())
                .toList();

        BigDecimal optionalTotal = configOptions.stream()
                .map(ConfigOptions::getOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalPrice = totalPrice.add(optionalTotal);

        Configuration configuration = Configuration.builder()
                .totalPrice(totalPrice)
                .product(product)
                .configuredOptions(configOptions)
                .build();
        configurationRepository.save(configuration);

        return ConfigurationResponseDto.builder()
                .productId(String.valueOf(configuration.getProduct().getId()))
                .totalPrice(totalPrice)
                .configuredPrice(optionalTotal)
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .configured(configOptions)
                .build();
    }

    @Override
    public ConfigurationResponseDto configuration(String productId, UUID categoryId) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        BigDecimal totalPrice = BigDecimal.valueOf(product.getProductPrice());

        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Configuration does not exist for the specified category"));

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        List<ConfigOptions> configOptions = compatibleOptions.stream()
                .filter(CompatibleOption::getIsIncluded)
                .map(option -> ConfigOptions.builder()
                        .optionId(String.valueOf(option.getId()))
                        .optionName(option.getName())
                        .optionPrice(option.getPrice())
                        .optionType(option.getType())
                        .build())
                .toList();

        BigDecimal optionalTotal = configOptions.stream()
                .map(ConfigOptions::getOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalPrice = totalPrice.add(optionalTotal);

        Configuration configuration = Configuration.builder()
                .totalPrice(totalPrice)
                .product(product)
                .configuredOptions(configOptions)
                .build();
        configurationRepository.save(configuration);

        return ConfigurationResponseDto.builder()
                .productId(String.valueOf(configuration.getProduct().getId()))
                .totalPrice(totalPrice)
                .configuredPrice(optionalTotal)
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .configured(configOptions)
                .build();
    }


}


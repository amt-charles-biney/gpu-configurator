package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final CompatibleOptionService compatibleOptionService;
    private final ProductRepository productRepository;
    private final CategoryConfigRepository categoryConfigRepository;
    private final ConfigurationRepository configurationRepository;


    @Override
    public ConfigurationResponseDto configuration(String productId, String components, Boolean warranty, Boolean save, String component_is_sizable) {
        String[] componentIds = components != null ? components.split(",") : new String[0];
        String[] component_is_sizableList = component_is_sizable.split(",");


        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        BigDecimal totalPrice = BigDecimal.valueOf(product.getProductPrice());

        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(product.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Configuration does not exist for the specified category"));

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        List<ConfigOptions> configOptions;

        if (components != null && !components.isEmpty()) {
            configOptions = compatibleOptions.stream()
                    .filter(option -> Arrays.asList(componentIds).contains(String.valueOf(option.getId())))
                    .map(option -> ConfigOptions.builder()
                            .optionId(String.valueOf(option.getId()))
                            .optionName(option.getName())
                            .optionPrice(option.getPrice())
                            .optionType(option.getType())
                            .isIncluded(option.getIsIncluded())
                            .build())
                    .toList();



        } else if (!component_is_sizable.isEmpty()) {
            configOptions = compatibleOptions.stream()
                    .filter(option -> Arrays.stream(component_is_sizableList)
                            .map(pair -> pair.split("_")[0])
                            .anyMatch(id -> id.equals(String.valueOf(option.getId()))))
                    .map(option -> ConfigOptions.builder()
                            .optionId(String.valueOf(option.getId()))
                            .optionName(option.getName())
                            .optionPrice(option.getPrice().multiply(option.getPriceIncrement()))
                            .optionType(option.getType())
                            .isIncluded(option.getIsIncluded())
                            .build())
                    .toList();


        } else {
            configOptions = compatibleOptions.stream()
                    .filter(CompatibleOption::getIsIncluded)
                    .map(option -> ConfigOptions.builder()
                            .optionId(String.valueOf(option.getId()))
                            .optionName(option.getName())
                            .optionPrice(option.getPrice())
                            .optionType(option.getType())
                            .isIncluded(option.getIsIncluded())
                            .build())
                    .toList();


        }


        BigDecimal optionalTotal = configOptions.stream()
                .map(ConfigOptions::getOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalPrice = totalPrice.add(optionalTotal);

        BigDecimal vat = BigDecimal.valueOf(25).divide(BigDecimal.valueOf(100)).multiply(totalPrice);

        Configuration configuration = Configuration.builder()
                .totalPrice(totalPrice)
                .product(product)
                .configuredOptions(configOptions)
                .build();

        if (save != null && save) {
            configurationRepository.save(configuration);
        }

        return ConfigurationResponseDto.builder()
                .productId(String.valueOf(configuration.getProduct().getId()))
                .totalPrice(totalPrice.add(vat))
                .warranty(warranty)
                .vat(vat)
                .configuredPrice(optionalTotal)
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .configured(configOptions)
                .build();
    }


}


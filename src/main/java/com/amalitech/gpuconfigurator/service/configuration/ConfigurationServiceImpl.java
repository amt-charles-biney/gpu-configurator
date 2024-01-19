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
    public ConfigurationResponseDto configuration(String productId, Boolean warranty, Boolean save, String components) {


        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        BigDecimal totalPrice = BigDecimal.valueOf(product.getProductPrice());

        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(product.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Configuration does not exist for the specified category"));

        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        List<ConfigOptions> configOptions;

        if (components != null && !components.isEmpty()) {
            String[] component_is_sizableList = components.split(",");
            configOptions = compatibleOptions.stream()
                    .filter(option -> Arrays.stream(component_is_sizableList)
                            .map(pair -> pair.split("_")[0])
                            .anyMatch(id -> id.equals(String.valueOf(option.getId()))))
                    .map(option -> {
                        if (Boolean.TRUE.equals(option.getIsMeasured())) {
                            String size = Arrays.stream(component_is_sizableList)
                                    .filter(pair -> pair.split("_")[0].equals(String.valueOf(option.getId())))
                                    .findFirst()
                                    .map(pair -> pair.split("_")[1])
                                    .orElseGet(() -> String.valueOf(option.getBaseAmount()));

                            BigDecimal sizeMultiplier = new BigDecimal(size).divide(option.getBaseAmount()).subtract(new BigDecimal(1));
                            BigDecimal calculatedPrice = option.getPrice().multiply(sizeMultiplier).multiply(new BigDecimal(option.getPriceFactor()));

                            return ConfigOptions.builder()
                                    .optionId(String.valueOf(option.getId()))
                                    .optionName(option.getName())
                                    .optionPrice(calculatedPrice)
                                    .optionType(option.getType())
                                    .baseAmount(option.getBaseAmount())
                                    .isIncluded(option.getIsIncluded())
                                    .isMeasured(option.getIsMeasured())
                                    .build();
                        } else {
                            return ConfigOptions.builder()
                                    .optionId(String.valueOf(option.getId()))
                                    .optionName(option.getName())
                                    .optionPrice(option.getPrice())
                                    .optionType(option.getType())
                                    .baseAmount(option.getBaseAmount())
                                    .isIncluded(option.getIsIncluded())
                                    .isMeasured(option.getIsMeasured())
                                    .build();
                        }
                    })
                    .toList();

        } else {
            configOptions = compatibleOptions.stream()
                    .filter(CompatibleOption::getIsIncluded)
                    .map(option -> ConfigOptions.builder()
                            .optionId(String.valueOf(option.getId()))
                            .optionName(option.getName())
                            .optionPrice(option.getPrice())
                            .optionType(option.getType())
                            .baseAmount(option.getBaseAmount())
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


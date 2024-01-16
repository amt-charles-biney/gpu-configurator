package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationRequestDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.configuration.ProductDetailsDto;
import com.amalitech.gpuconfigurator.dto.configuration.configOptionDto;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationOptionsRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import com.amalitech.gpuconfigurator.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ProductService productService;
    private final CompatibleOptionService compatibleOptionService;

    private final CategoryConfigRepository categoryConfigRepository;
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationOptionsRepository configurationOptionsRepository;

    @Override
    public ConfigurationResponseDto addConfiguration(ConfigurationRequestDto request) {
        var product = productService.getProduct(request.productId());
        ProductDetailsDto productDetails = ProductDetailsDto.builder()
                .id(product.id())
                .productPrice(product.productPrice())
                .productName(product.productName())
                .build();

        BigDecimal totalPrice = productDetails.getProductPrice();

        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(request.categoryId())).orElseThrow(() -> new EntityNotFoundException("config does not exist"));
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        Map<String, List<configOptionDto>> compatibleGroupedByType = compatibleOptions.stream()
                .collect(Collectors.groupingBy(
                        CompatibleOption::getType,
                        Collectors.mapping(option -> configOptionDto.builder()
                                        .optionId(option.getId().toString())
                                        .optionType(option.getType())
                                        .optionPrice(option.getPrice())
                                        .optionName(option.getName())
                                        .build(),
                                Collectors.toList())));

        BigDecimal optionsTotalPrice = compatibleOptions.stream()
                .map(CompatibleOption::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalPrice = totalPrice.add(optionsTotalPrice);

        if (request.configOptionId() != null) {
            String configOptionType = request.configOptionId().getType();
            String configOptionId = request.configOptionId().getId();

            if (!compatibleGroupedByType.containsKey(configOptionType)) {
                throw new IllegalArgumentException("Invalid configOptionId type");
            }

            List<configOptionDto> filteredOptions = compatibleGroupedByType.get(configOptionType).stream()
                    .filter(option -> option.getOptionId().equals(configOptionId))
                    .toList();

            if (filteredOptions.isEmpty()) {
                throw new IllegalArgumentException("Invalid configOptionId id");
            }

            compatibleGroupedByType.put(configOptionType, filteredOptions);
        }

        Configuration configuration = new Configuration();
        configuration.setConfigured(configurationOptionsRepository.findAll());
        configuration.setTotalPrice(totalPrice);
        configurationRepository.save(configuration);

        return ConfigurationResponseDto.builder()
                .totalPrice(totalPrice)
                .product(productDetails)
                .configured(compatibleGroupedByType)
                .build();
    }

}


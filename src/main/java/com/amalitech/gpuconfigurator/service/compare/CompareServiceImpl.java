package com.amalitech.gpuconfigurator.service.compare;

import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {

    private final ProductRepository productRepository;
    private final CategoryConfigRepository categoryConfigRepository;

    @Override
    public ProductCompareResponse getProductCompare(String productIdString) {
        UUID productId = UUID.fromString(productIdString);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("product does not exist"));

        return this.buildProduct(product);
    }

    @Override
    public List<ProductCompareResponse> getProductCompareList(List<String> productIds) {
        if (productIds.isEmpty()) return Collections.emptyList();

        List<UUID> productUUIDs = productIds.stream().map(UUID::fromString).toList();
        List<Product> products = productRepository.findAllById(productUUIDs);
        return products
                .stream()
                .map(this::buildProduct)
                .toList();
    }

    private Map<String, String> getIncludeCategoryConfigOptions(Product product) {
        List<CompatibleOption> compatibleOptionList = categoryConfigRepository
                .findById(product.getCategory().getCategoryConfig().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category configuration not found"))
                .getCompatibleOptions();

        Map<String, String> includedOptions = new HashMap<>();
        for (CompatibleOption option : compatibleOptionList) {
            if (option.getIsIncluded()) {
                String attributeName = option.getAttributeOption().getAttribute().getAttributeName().toLowerCase();
                String optionName = option.getAttributeOption().getOptionName();
                includedOptions.merge(attributeName, optionName, (oldValue, newValue) -> oldValue + ", " + newValue);
            }
        }
        return includedOptions;
    }


    private ProductCompareResponse buildProduct(Product product) {
        Map<String, String> options = this.getIncludeCategoryConfigOptions(product);

        return ProductCompareResponse
                .builder()
                .coverImage(product.getProductCase().getCoverImageUrl())
                .description(product.getProductDescription())
                .productName(product.getProductName())
                .productId(product.getId().toString())
                .productPrice(product.getTotalProductPrice())
                .options(options)
                .productCase(product.getProductCase().getName())
                .build();
    }

}

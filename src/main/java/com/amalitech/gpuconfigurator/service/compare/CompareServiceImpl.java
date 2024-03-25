package com.amalitech.gpuconfigurator.service.compare;

import com.amalitech.gpuconfigurator.dto.compare.ProductCompareResponse;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {

    private final ProductRepository productRepository;
    private final CategoryConfigRepository categoryConfigRepository;

    @Override
    public ProductCompareResponse getProductCompare(String productId) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new EntityNotFoundException("product does not exist"));

        return this.buildProduct(product);

    }

    @Override
    public List<ProductCompareResponse> getProductCompareList(List<String> productIds) {
        if(productIds.isEmpty()) return new ArrayList<>();

        List<UUID> productUUIDs = productIds.stream().map(UUID::fromString).toList();
        List<Product> products = productRepository.findAllById(productUUIDs);
        return products
                .stream()
                .map(this::buildProduct)
                .toList();
    }

    private Map<String, String> getIncludeCategoryConfigOptions(Product product) {
        List<CompatibleOption> compatibleOptionList = categoryConfigRepository
                .findById(product
                        .getCategory()
                        .getCategoryConfig()
                        .getId())
                .get()
                .getCompatibleOptions();

        return compatibleOptionList.stream()
                .filter(option -> option.getIsIncluded())
                .collect(Collectors.groupingBy(
                        option -> option.getAttributeOption().getAttribute().getAttributeName(),
                        Collectors.mapping(
                                option -> option.getAttributeOption().getOptionName(),
                                Collectors.joining(", ")
                        )
                ));

    }


    private ProductCompareResponse buildProduct(Product product) {
        Map<String, String> options = this.getIncludeCategoryConfigOptions(product);

        return ProductCompareResponse
                .builder()
                .coverImage(product.getProductCase().getCoverImageUrl())
                .description(product.getProductDescription())
                .productName(product.getProductName())
                .productId(product.getProductId().toString())
                .productPrice(product.getTotalProductPrice())
                .options(options)
                .productCase(product.getProductCase().getName())
                .build();
    }

}

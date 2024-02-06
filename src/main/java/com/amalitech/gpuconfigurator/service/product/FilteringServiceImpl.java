package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilteringServiceImpl implements FilteringService {

    private final ProductRepository productRepository;
    private final ConfigOptionsFiltering configOptionsFiltering;


    public List<Product> filterProduct(String brand, String price, String productType, String processor) {

        Specification<Product> spec = Specification.where(null);

        if (brand != null && !brand.isEmpty()) {
            String[] brandNames = brand.split(",");
            if (brandNames.length == 1) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("productBrand"), brandNames[0])
                );
            } else {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(root.get("productBrand").in((Object[]) brandNames))
                );
            }
        }

        if (price != null && !price.isEmpty()) {
            List<Specification<Product>> newSpec = getSpecifications(price);

            spec = spec.and(newSpec.stream().reduce(Specification::or).orElse(null));
        }


        if (productType != null && !productType.isEmpty()) {
            List<UUID> matchingProductIds = configOptionsFiltering.getProductTypes(productType);
            if (!matchingProductIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProductIds));
            } else {
                return Collections.emptyList();
            }
        }

        if (processor != null && !processor.isEmpty()) {
            List<UUID> matchingProcessorIds = configOptionsFiltering.getProcessor(processor);
            if (!matchingProcessorIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProcessorIds));
            } else {
                return Collections.emptyList();
            }
        }

        return productRepository.findAll(spec);
    }

    @NotNull
    private static List<Specification<Product>> getSpecifications(String price) {
        String[] priceList = price.split(",");
        String productPrice = "productPrice";

        List<Specification<Product>> newSpec = new ArrayList<>();

        for (String priceItem : priceList) {
            if (priceItem.startsWith(">")) {
                double minValue = Double.parseDouble(priceItem.substring(1));
                newSpec.add((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(productPrice), minValue)
                );
            } else {
                var ranges = priceItem.split("-");
                newSpec.add((root, query, criteriaBuilder) ->
                        criteriaBuilder.between(root.get(productPrice), Double.parseDouble(ranges[0]), Double.parseDouble(ranges[1]))
                );
            }
        }
        return newSpec;
    }

}

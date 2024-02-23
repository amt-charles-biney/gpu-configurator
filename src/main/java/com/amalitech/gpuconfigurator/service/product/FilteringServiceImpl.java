package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FilteringServiceImpl implements FilteringService {

    private final ProductRepository productRepository;
    private final ConfigOptionsFiltering configOptionsFiltering;


    public List<Product> filterProduct(String productCase, String price, String productType, String processor, String category) {

        Specification<Product> spec = Specification.where(null);

        if (productCase != null && !productCase.isEmpty()) {
            String[] productCaseNames = productCase.split(",");
            if (productCaseNames.length == 1) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("productCase").get("name"), productCaseNames[0])
                );
            } else {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(root.get("productCase").get("name").in((Object[]) productCaseNames))
                );
            }
        }

        if (category != null && !category.isEmpty()) {
            String[] productCategory = category.split(",");
            if (productCategory.length == 1) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("category").get("categoryName"), productCategory[0])
                );
            } else {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(root.get("category").get("categoryName").in((Object[]) productCategory))
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

<<<<<<< HEAD
=======
        if (brand != null && !brand.isEmpty()) {
            Set<UUID> matchingProductIds = configOptionsFiltering.getBrand(brand);
            if (!matchingProductIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProductIds));
            } else {
                return Collections.emptyList();
            }
        }

>>>>>>> deb5714 (feat: out of stock and unassigned category will not show on filter)
        if (processor != null && !processor.isEmpty()) {
            List<UUID> matchingProcessorIds = configOptionsFiltering.getProcessor(processor);
            if (!matchingProcessorIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProcessorIds));
            } else {
                return Collections.emptyList();
            }
        }

        return productRepository.findAll(spec).stream().filter(product -> !"unassigned".equals(product.getCategory().getCategoryName()) && !product.getInStock().equals(0)).toList();
    }

    private static List<Specification<Product>> getSpecifications(String price) {
        String[] priceList = price.split(",");
        String productPrice = "totalProductPrice";

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

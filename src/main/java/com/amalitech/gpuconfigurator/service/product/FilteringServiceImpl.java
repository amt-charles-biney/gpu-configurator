package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilteringServiceImpl implements FilteringService{

    private final ProductRepository productRepository;

    public List<Product> filterProduct(String brand, String price) {
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
            String[] priceList = price.split(",");
            String productPrice = "productPrice";
            for (String priceItem : priceList) {
                if (priceItem.contains("-")) {
                    String[] rangeValues = priceItem.split("-");
                    if (rangeValues.length == 2) {
                        spec = spec.and((root, query, criteriaBuilder) ->
                                criteriaBuilder.between(root.get(productPrice), Double.parseDouble(rangeValues[0]), Double.parseDouble(rangeValues[1]))
                        );
                    }
                } else if (priceItem.startsWith(">")) {
                    double minValue = Double.parseDouble(priceItem.substring(1));
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThan(root.get(productPrice), minValue)
                    );
                } else if (priceItem.startsWith("<")) {
                    double maxValue = Double.parseDouble(priceItem.substring(1));
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThan(root.get(productPrice), maxValue)
                    );
                } else {
                    spec = spec.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(productPrice), Double.parseDouble(priceItem))
                    );
                }
            }
        }

        return productRepository.findAll(spec);
    }

}

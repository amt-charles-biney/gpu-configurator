package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeaturedServiceImpl implements FeaturedService {
    private final ProductRepository productRepository;

    @Override
    public List<FeaturedProductDto> getAllFeaturedProduct() {
        var products = productRepository.getFeaturedProduct().orElse(Collections.emptyList());

        return products.stream()
                .map(product -> FeaturedProductDto.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .coverImage(product.getProductCase().getCoverImageUrl())
                        .productPrice(product.getTotalProductPrice())
                        .productBrand(product.getProductCase().getName())
                        .productAvailability(product.getCategory().getCategoryConfig().getCompatibleOptions()
                                .stream()
                                .filter(CompatibleOption::getIsIncluded)
                                .allMatch(includedOption ->
                                        includedOption.getAttributeOption().getInStock() != null
                                                && includedOption.getAttributeOption().getInStock() > 0))
                        .build())
                .toList();
    }

    @Override
    public FeaturedResponseDto addFeaturedProduct(UUID id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Product does not exist")
            );

            if (product.getCategory().getCategoryName().equals("unassigned")) {
                throw new NotFoundException("This product has no category");
            }

            if (Boolean.TRUE.equals(product.getFeatured())) {
                return FeaturedResponseDto.builder().message("Product is already Featured").build();
            }
            product.setFeatured(true);
            productRepository.save(product);
            return FeaturedResponseDto.builder().message("Now a featured Product").build();

        } catch (NotFoundException e) {
            throw new NotFoundException("Product does not exist");
        }
    }

    @Override
    public FeaturedResponseDto removeFeaturedProduct(UUID id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Product does not exist")
            );

            if (Boolean.FALSE.equals(product.getFeatured())) {
                return FeaturedResponseDto.builder().message("Product is not featured").build();
            }
            product.setFeatured(false);
            productRepository.save(product);
            return FeaturedResponseDto.builder().message("Product is no more featured").build();

        } catch (NotFoundException e) {
            throw new NotFoundException("Product does not exist");
        }
    }

}

package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.FeaturedProductAbstraction;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeaturedServiceImpl implements FeaturedService {
    private final ProductRepository productRepository;
    private final ConfigurationRepository configurationRepository;
    private final WishListRepository wishListRepository;

    @Override
    public List<FeaturedProductDto> getAllFeaturedProduct(User user, UserSession userSession) {
        var products = productRepository.getFeaturedProduct().orElse(Collections.emptyList());

        Set<UUID> productsInWishList = getIdsOfProductsInWishList(products, user, userSession);

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
                        .isWishListItem(productsInWishList.contains(UUID.fromString(product.getId())))
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

    private Set<UUID> getIdsOfProductsInWishList(List<FeaturedProductAbstraction> products, User user, UserSession userSession) {
        Optional<WishList> optionalWishList = getUserOrSessionWishList(user, userSession);
        if (optionalWishList.isEmpty()) {
            return Collections.emptySet();
        }

        List<Configuration> wishListItems = configurationRepository.findByWishListIdAndProductIdIn(
                optionalWishList.get().getId(),
                products.stream().map(product -> UUID.fromString(product.getId())).toList()
        );

        Set<UUID> productsInWishList = new HashSet<>();

        for (FeaturedProductAbstraction product : products) {
            Set<CompatibleOption> productVariants = product.getCategory()
                    .getCategoryConfig()
                    .getCompatibleOptions()
                    .stream()
                    .filter(CompatibleOption::getIsIncluded)
                    .collect(Collectors.toSet());

            for (Configuration item : wishListItems) {
                if (!item.getProduct().getId().equals(UUID.fromString(product.getId()))) {
                    continue;
                }

                if (item.getProduct().getCategory().getCategoryName().equals("unassigned")) {
                    continue;
                }

                boolean isVariantInProductBaseConfiguration = true;
                for (ConfigOptions configOption : item.getConfiguredOptions()) {
                    if (configOption.getIsMeasured()) {
                        isVariantInProductBaseConfiguration = isVariantInProductBaseConfiguration
                                && productVariants.stream().anyMatch(option -> option.getId().toString().equals(configOption.getOptionId())
                                && option.getSize().equals(Integer.valueOf(configOption.getSize())));
                    } else {
                        isVariantInProductBaseConfiguration = isVariantInProductBaseConfiguration && configOption.isIncluded();
                    }
                }
                if (isVariantInProductBaseConfiguration) {
                    productsInWishList.add(UUID.fromString(product.getId()));
                    break;
                }
            }
        }

        return productsInWishList;
    }

    private Optional<WishList> getUserOrSessionWishList(User user, UserSession userSession) {
        if (user != null) {
            return wishListRepository.findByUserId(user.getId());
        }
        return wishListRepository.findByUserSessionId(userSession.getId());
    }

}

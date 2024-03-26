package com.amalitech.gpuconfigurator.service.wishlist;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.wishlist.AddWishListItemResponse;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.WishListRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import com.amalitech.gpuconfigurator.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ConfigurationService configurationService;
    private final ConfigurationRepository configuredProductRepository;
    private final ProductRepository productRepository;

    @Override
    public AddWishListItemResponse addWishListItem(UUID productId, String components, User user, UserSession userSession) {
        WishList wishList = getUserOrSessionWishList(user, userSession);

        return new AddWishListItemResponse(
                configurationService.saveConfiguration(String.valueOf(productId), false, components, null, wishList),
                "Product added to wish list successfully."
        );
    }

    @Override
    public Page<ConfigurationResponseDto> getWishListItems(int page, int size, User user, UserSession userSession) {
        WishList wishList = getUserOrSessionWishList(user, userSession);

        Pageable pageable = PageRequest.of(page, size);

        return configuredProductRepository.findByWishListId(wishList.getId(), pageable)
                .map((new ResponseMapper())::mapConfigurationToConfigurationResponseDto);
    }

    @Override
    public GenericResponse removeWishListItem(UUID productIdOrConfiguredProductId, User user, UserSession userSession) {
        WishList wishList = getUserOrSessionWishList(user, userSession);

        Optional<Product> optionalProduct = productRepository.findById(productIdOrConfiguredProductId);
        if (optionalProduct.isPresent()) {
            removeWishListItemGivenProductId(optionalProduct.get(), wishList);
        } else {
            configuredProductRepository
                    .findByIdAndWishListId(productIdOrConfiguredProductId, wishList.getId())
                    .ifPresent(configuredProductRepository::delete);
        }

        return new GenericResponse(200, "Product removed from wish list successfully.");
    }

    @Override
    public void mergeUserAndSessionWishLists(User user, UserSession userSession) {
        Optional<WishList> optionalUserWishList = wishListRepository.findByUserId(user.getId());
        Optional<WishList> optionalSessionWishList = wishListRepository.findByUserSessionId(userSession.getId());

        if (optionalUserWishList.isEmpty()) {
            if (optionalSessionWishList.isPresent()) {
                WishList wishList = optionalSessionWishList.get();
                wishList.setUser(user);
                wishList.setUserSession(null);
                wishListRepository.save(wishList);
            }
        } else {
            if (optionalSessionWishList.isPresent()) {
                List<Configuration> configuredProducts = optionalSessionWishList.get().getConfiguredProducts().stream().toList();

                for (Configuration configuredProduct : configuredProducts) {
                    configuredProduct.setWishList(optionalUserWishList.get());
                }

                configuredProductRepository.saveAll(configuredProducts);
            }
        }
    }

    private void removeWishListItemGivenProductId(Product product, WishList wishList) {
        List<Configuration> wishListItems = configuredProductRepository.findByWishListIdAndProductId(wishList.getId(), product.getId());
        Set<CompatibleOption> productVariants = product.getCategory()
                .getCategoryConfig()
                .getCompatibleOptions()
                .stream()
                .filter(CompatibleOption::getIsIncluded)
                .collect(Collectors.toSet());
        Set<Configuration> wishListItemsToDelete = wishListItems.stream()
                .filter(item -> {
                    for (ConfigOptions configOption : item.getConfiguredOptions()) {
                        for (CompatibleOption productOption : productVariants) {
                            if (productOption.getId().toString().equals(configOption.getOptionId())) {
                                continue;
                            }
                            return !productOption.getIsMeasured() || productOption.getSize().equals(Integer.valueOf(configOption.getSize()));
                        }
                    }
                    return false;
                }).collect(Collectors.toSet());
        configuredProductRepository.deleteAll(wishListItemsToDelete);
    }

    private WishList getUserOrSessionWishList(User user, UserSession userSession) {
        if (user != null) {
            Optional<WishList> userWishList = wishListRepository.findByUserId(user.getId());
            if (userWishList.isEmpty()) {
                WishList newWishList = new WishList();
                newWishList.setUser(user);
                return wishListRepository.save(newWishList);
            }
            return userWishList.get();
        }

        Optional<WishList> sessionWishList = wishListRepository.findByUserSessionId(userSession.getId());
        if (sessionWishList.isEmpty()) {
            WishList newWishList = new WishList();
            newWishList.setUserSession(userSession);
            return wishListRepository.save(newWishList);
        }
        return sessionWishList.get();
    }
}

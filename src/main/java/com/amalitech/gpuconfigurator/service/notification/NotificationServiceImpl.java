package com.amalitech.gpuconfigurator.service.notification;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.VariantStockLeastDto;
import com.amalitech.gpuconfigurator.dto.notification.NotificationResponse;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.WishList;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.WishListRepository;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {
    private final WishListRepository wishListRepository;
    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryConfigServiceImpl configService;
    private final AttributeServiceImpl attributeService;

    public Map<User, Product> getUsersToNotifyOfStockUpdate() {
        Map<User, Product> notifyUsers = new HashMap<>();
        List<Product> products = getAllNotifiableProducts();
        List<WishList> wishLists = wishListRepository.findAll();

        for (WishList wishList : wishLists) {
            Set<Configuration> configurations = wishList.getConfiguredProducts();
            for (Configuration configuration : configurations) {
                if (products.contains(configuration.getProduct())) {
                    notifyUsers.put(wishList.getUser(), configuration.getProduct());
                    break;
                }
            }
        }

        return notifyUsers;
    }

    public List<Product> getAllNotifiableProducts() {
        List<Product> products = new ArrayList<>();
        List<CategoryConfig> categoryConfigs = categoryConfigRepository.findAll();

        for (CategoryConfig categoryConfig : categoryConfigs) {
            int leastStock = configService.getTotalLeastStock(categoryConfig.getCompatibleOptions());
            if (leastStock > 5) {
                products.addAll(categoryConfig.getCategory().getProducts());
            }
        }
        return products;
    }


    public NotificationResponse getAllAdminFixNotifications() {
        List<AttributeResponse> attributeResponses = attributeService.getAllAttributesLowInStock();

        return NotificationResponse
                .builder()
                .count(attributeResponses.size())
                .attributeResponseList(attributeResponses)
                .build();
    }
}

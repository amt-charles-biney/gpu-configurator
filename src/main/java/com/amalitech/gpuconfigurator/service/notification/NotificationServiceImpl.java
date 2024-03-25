package com.amalitech.gpuconfigurator.service.notification;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigDisplay;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryListResponse;
import com.amalitech.gpuconfigurator.dto.notification.NotificationItemResponse;
import com.amalitech.gpuconfigurator.dto.notification.NotificationResponse;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.WishListRepository;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {
    private final WishListRepository wishListRepository;
    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryConfigServiceImpl configService;
    private final CategoryRepository categoryRepository;
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
        List<NotificationItemResponse<AttributeResponse>> attributeNotificationResponses = attributeService.getAllAttributesLowInStock()
                .stream()
                .map(this::buildNotificationItemResponse)
                .collect(Collectors.toList());

        List<NotificationItemResponse> unassignedProducts = new ArrayList<>();

        Optional<Category> category = categoryRepository.findByCategoryName("unassigned");

        if (category.isPresent())
            unassignedProducts = category.get().getProducts().stream().map(this::buildNotificationItemResponse).toList();

        List<NotificationItemResponse> getRequiredCategories = this.getRequiredCategories();


        Integer count = attributeNotificationResponses.size() + unassignedProducts.size() + getRequiredCategories.size();

        return NotificationResponse
                .builder()
                .count(count)
                .lowOrZeroStock(attributeNotificationResponses)
                .unassignedProducts(unassignedProducts)
                .requiredCategories(getRequiredCategories)
                .build();
    }


    private List<NotificationItemResponse> getRequiredCategories() {
        Set<NotificationItemResponse> requiredCategories = new HashSet<>();
        List<Attribute> attributes = attributeService.getAllAttributesByRequiredTrue();
        List<CategoryListResponse> categoryListResponses = configService.getCategoryListResponses();

        for (CategoryListResponse categoryListResponse : categoryListResponses) {
            for (Attribute attribute : attributes) {
                if (!categoryListResponse.config().containsKey(attribute.getAttributeName())
                        || categoryListResponse.config().get(attribute.getAttributeName()).stream().filter(CategoryConfigDisplay::isIncluded).toList().isEmpty()) {
                    requiredCategories.add(this.buildNotificationItemResponse(categoryListResponse, attribute.getAttributeName()));
                }
            }
        }

        return requiredCategories.stream().toList();
    }

    private NotificationItemResponse<AttributeResponse> buildNotificationItemResponse(AttributeResponse attributeResponse) {
        AttributeOptionResponseDto getAttributeOptionLowInStock = getNotificationAttributeLowInStock(attributeResponse);

        return new NotificationItemResponse<AttributeResponse>(
                getAttributeOptionLowInStock.optionName(),
                getAttributeOptionLowInStock.id(),
                "" + getAttributeOptionLowInStock.optionName() + " has " + getAttributeOptionLowInStock.inStock() + " stocks.",
                attributeResponse
        );
    }

    private NotificationItemResponse buildNotificationItemResponse(Product product) {
        return new NotificationItemResponse<>(
                product.getProductName(),
                product.getId().toString(),
                "" + product.getProductName() + " is unassigned",
                null
        );
    }

    private NotificationItemResponse buildNotificationItemResponse(CategoryListResponse categoryListResponse, String attributeName) {
        return new NotificationItemResponse<>(
                categoryListResponse.name(),
                categoryListResponse.id(),
                "" + attributeName + " is required in " + categoryListResponse.name(),
                null
        );
    }

    private AttributeOptionResponseDto getNotificationAttributeLowInStock(AttributeResponse attribute) {
        return attribute.attributeOptions().stream()
                .min(Comparator.comparingInt(option -> option.inStock()))
                .orElse(null);
    }
}

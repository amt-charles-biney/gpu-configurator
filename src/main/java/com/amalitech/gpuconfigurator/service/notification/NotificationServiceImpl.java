package com.amalitech.gpuconfigurator.service.notification;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigDisplay;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryListResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.VariantStockLeastDto;
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

    public Map<User, Product> getUsersToNotifyOfStockUpdate(String attributeName) {
        Map<User, Product> notifyUsers = new HashMap<>();

        List<Product> products = getAllNotifiableProducts(attributeName);
        List<WishList> wishLists = wishListRepository.findAll();

        for (WishList wishList : wishLists) {
            Set<Configuration> configurations = wishList.getConfiguredProducts();
            for (Configuration configuration : configurations) {

                boolean isProductPresent = this.isProductPresent(configuration, products);
                if (isProductPresent) {
                    notifyUsers.put(wishList.getUser(), configuration.getProduct());
                    break;
                }
            }
        }

        return notifyUsers;
    }

    public boolean isProductPresent(Configuration configuration, List<Product> products) {
        Optional<Product> foundProduct = this.findConfigurationProductInProducts(configuration, products);
        return foundProduct.isPresent();
    }

    public Optional<Product> findConfigurationProductInProducts(Configuration configuration, List<Product> products) {
        Product product = configuration.getProduct();
        if (product == null) {
            return Optional.empty();
        }

        return products.stream()
                .filter(p -> Objects.equals(p.getId(), product.getId()))
                .findFirst();
    }


    public List<Product> getAllNotifiableProducts(String attributeName) {
        List<Product> products = new ArrayList<>();
        List<CategoryConfig> categoryConfigs = categoryConfigRepository.findAll();

        for (CategoryConfig categoryConfig : categoryConfigs) {
            boolean leastStockGreaterThan = this.isLeastStockGreaterThan(categoryConfig.getCompatibleOptions(), attributeName);
            if (leastStockGreaterThan) {
                products.addAll(categoryConfig.getCategory().getProducts());
            }
        }
        return products;
    }

    public boolean isLeastStockGreaterThan(List<CompatibleOption> compatibleOptions, String attributeName) {
        List<VariantStockLeastDto> leastStockVariants = configService.getTotalLeastStocksInclusive(compatibleOptions);

        if (leastStockVariants.size() > 1) return false;
        if (leastStockVariants.isEmpty()) return true;

        return leastStockVariants.getFirst().name().equals(attributeName);

    }

    public NotificationResponse getAllAdminFixNotifications() {
        List<NotificationItemResponse<AttributeResponse>> attributeNotificationResponses = getAttributeNotificationResponses();
        List<NotificationItemResponse> unassignedProducts = this.getUnassignedProduct();
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

    private List<NotificationItemResponse> getUnassignedProduct() {
        return categoryRepository.findByCategoryName("unassigned")
                .map(Category::getProducts)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildNotificationItemResponse)
                .toList();
    }

    private List<NotificationItemResponse<AttributeResponse>> getAttributeNotificationResponses() {
        return attributeService.getAllAttributesLowInStock()
                .stream()
                .map(this::buildNotificationItemResponse)
                .collect(Collectors.toList());
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

        return new NotificationItemResponse<>(
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

package com.amalitech.gpuconfigurator.service.recommendation;

import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.FeaturedProductAbstraction;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final ProductRepository productRepository;

    @Override
    public List<FeaturedProductDto> getRecommendation(Principal principal, UserSession userSession) {
        User user = getUserFromPrincipal(principal);

        List<FeaturedProductDto> recommendedProducts = new ArrayList<>();

        Product product = getProductFromUserOrSession(user, userSession);

        if (product != null) {
            List<FeaturedProductAbstraction> prods = productRepository.selectAllProductByCategory(product.getCategory().getId());

            // Filter out the product that is already in the user's cart
            prods.removeIf(prod -> user != null && user.getCart().getConfiguredProducts().stream()
                    .anyMatch(config -> config.getProduct().getId().toString().equals(prod.getId())));

            // Shuffle the list of products
            Collections.shuffle(prods);

            // Add the first 3 random products to the list
            int count = Math.min(3, prods.size()); // Ensure not to exceed the size of the list
            for (int i = 0; i < count; i++) {
                FeaturedProductAbstraction featuredProduct = prods.get(i);
                recommendedProducts.add(createFeaturedProductDto(featuredProduct));
            }
        }

        return recommendedProducts;
    }


    private User getUserFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        return null;
    }

    private Product getProductFromUserOrSession(User user, UserSession userSession) {
        if (user != null) {
            return user.getCart().getConfiguredProducts().stream()
                    .findFirst()
                    .map(Configuration::getProduct)
                    .orElse(null);
        } else if (userSession != null) {
            return userSession.getCart().getConfiguredProducts().stream()
                    .findFirst()
                    .map(Configuration::getProduct)
                    .orElse(null);
        }
        return null;
    }

    private FeaturedProductDto createFeaturedProductDto(FeaturedProductAbstraction featuredProduct) {
        return FeaturedProductDto.builder()
                .id(featuredProduct.getId())
                .productName(featuredProduct.getProductName())
                .coverImage(featuredProduct.getProductCase().getCoverImageUrl())
                .productPrice(featuredProduct.getTotalProductPrice())
                .productBrand(featuredProduct.getProductCase().getName())
                .build();
    }
}

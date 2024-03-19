package com.amalitech.gpuconfigurator.service.wishlist;

import com.amalitech.gpuconfigurator.dto.wishlist.AddWishListItemResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.WishList;
import com.amalitech.gpuconfigurator.repository.WishListRepository;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ConfigurationService configurationService;

    @Override
    public AddWishListItemResponse addWishListItem(UUID productId, String components, User user, UserSession userSession) {
        WishList wishList = getUserOrSessionWishList(user, userSession);

        return new AddWishListItemResponse(
                configurationService.saveConfiguration(String.valueOf(productId), false, components, null, wishList),
                "Product added to wish list successfully."
        );
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

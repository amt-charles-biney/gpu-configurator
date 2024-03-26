package com.amalitech.gpuconfigurator.service.wishlist;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.wishlist.AddWishListItemResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WishListService {
    AddWishListItemResponse addWishListItem(UUID productId, String components, User user, UserSession userSession);

    Page<ConfigurationResponseDto> getWishListItems(int page, int size, User user, UserSession userSession);

    GenericResponse removeWishListItem(UUID productIdOrConfiguredProductId, User user, UserSession userSession);

    void mergeUserAndSessionWishLists(User user, UserSession userSession);
}

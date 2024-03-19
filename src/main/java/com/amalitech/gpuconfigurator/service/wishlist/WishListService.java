package com.amalitech.gpuconfigurator.service.wishlist;

import com.amalitech.gpuconfigurator.dto.wishlist.AddWishListItemResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;

import java.util.UUID;

public interface WishListService {
    AddWishListItemResponse addWishListItem(UUID productId, String components, User user, UserSession userSession);
}

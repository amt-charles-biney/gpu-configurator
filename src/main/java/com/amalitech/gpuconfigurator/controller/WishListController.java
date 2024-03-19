package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.dto.wishlist.AddWishListItemResponse;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.wishlist.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @PostMapping("/v1/wishlists/add-item/{productId}")
    public ResponseEntity<AddWishListItemResponse> addWishListItem(
            @PathVariable UUID productId,
            @RequestParam(required = false) String components,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        User user = principal == null
                ? null
                : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        AddWishListItemResponse response = wishListService.addWishListItem(productId, components, user, userSession);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/v1/wishlists")
    public ResponseEntity<Page<ConfigurationResponseDto>> getWishListItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        User user = principal == null
                ? null
                : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return ResponseEntity.ok(wishListService.getWishListItems(page, size, user, userSession));
    }
}

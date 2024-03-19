package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.service.wishlist.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;
}

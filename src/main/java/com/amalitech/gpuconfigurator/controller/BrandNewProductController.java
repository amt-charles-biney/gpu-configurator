package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.product.FeaturedProductDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandNewProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get brand new products",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/new")
    @ResponseStatus(HttpStatus.OK)
    public List<FeaturedProductDto> getBrandNewProducts(
            @RequestAttribute("userSession") UserSession userSession,
            Principal principal
    ) {
        User user = principal == null ? null : (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return productService.getNewProducts(user, userSession);
    }
}

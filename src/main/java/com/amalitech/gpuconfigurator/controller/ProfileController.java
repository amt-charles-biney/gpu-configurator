package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.dto.profile.ProfileShippingRequest;
import com.amalitech.gpuconfigurator.dto.profile.ProfileShippingResponse;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @CrossOrigin
    @PutMapping("/basic-info")
    public ResponseEntity<BasicInformationResponse> updateBasicInformation(
            @Validated @RequestBody BasicInformationRequest dto,
            Principal principal) {
        return ResponseEntity.ok(profileService.updateBasicInformation(dto, principal));
    }

    @CrossOrigin
    @GetMapping("/basic-info")
    public ResponseEntity<BasicInformationResponse> getBasicInformation(Principal principal) {
        return ResponseEntity.ok(profileService.getUserProfile(principal));
    }

    @CrossOrigin
    @PostMapping("/password")
    public ResponseEntity<GenericResponse> updateUserPassword(
            @Validated @RequestBody UserPasswordRequest dto,
            Principal principal) throws InvalidPasswordException {
        GenericResponse response = profileService.updateUserPassword(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/shipping-info")
    public ResponseEntity<ProfileShippingResponse> getUserShippingInformation(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(profileService.getUserShippingInformation(user));
    }

    @PostMapping("/shipping-info")
    public ResponseEntity<ShippingResponse> addUserShippingInformation(
            @RequestBody @Valid ProfileShippingRequest dto,
            Principal principal
    ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(profileService.addUserShippingInformation(dto, user));
    }
}

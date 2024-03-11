package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("/v1/profile/basic-info")
    public ResponseEntity<BasicInformationResponse> updateBasicInformation(
            @Validated @RequestBody BasicInformationRequest dto,
            Principal principal) {
        return ResponseEntity.ok(profileService.updateBasicInformation(dto, principal));
    }

    @GetMapping("/v1/profile/basic-info")
    public ResponseEntity<BasicInformationResponse> getBasicInformation(Principal principal) {
        return ResponseEntity.ok(profileService.getUserProfile(principal));
    }

    @PostMapping("/v1/profile/password")
    public ResponseEntity<GenericResponse> updateUserPassword(
            @Validated @RequestBody UserPasswordRequest dto,
            Principal principal) throws InvalidPasswordException {
        GenericResponse response = profileService.updateUserPassword(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.UserPasswordRequest;
import com.amalitech.gpuconfigurator.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("/basic-info")
    public ResponseEntity<String> updateBasicInformation(
            @Validated @RequestBody BasicInformationRequest dto,
            Principal principal
    ) {
        profileService.updateBasicInformation(dto, principal);
        return ResponseEntity.ok("Basic information updated successfully");
    }

    @PostMapping("/password")
    public ResponseEntity<String> updateUserPassword(
            @Validated @RequestBody UserPasswordRequest dto,
            Principal principal
    ) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().body("Password and password confirmation are not equal");
        }
        if (profileService.updateUserPassword(dto, principal)) {
            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.badRequest().body("Wrong user password");
    }
}

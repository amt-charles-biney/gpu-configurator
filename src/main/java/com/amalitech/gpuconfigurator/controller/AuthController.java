package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userServiceImpl;

    @PostMapping("/v1/auth/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpDto request) throws MessagingException {
        userServiceImpl.signup(request);
        return ResponseEntity.ok("verification email has been sent");
    }

    @PostMapping("/v1/auth/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestBody VerifyUserDto request) throws MessagingException {

        AuthenticationResponse user = userServiceImpl.verifyUserSignup(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request) {
        return ResponseEntity.ok(userServiceImpl.login(request));
    }

    @PostMapping("/v1/auth/reset-password")
    public ResponseEntity<String> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) throws MessagingException {
        userServiceImpl.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("Email with otp sent");
    }

    @PostMapping("/v1/auth/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        userServiceImpl.verifyResetOtp(verifyOtpDTO);
        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/v1/auth/change-password")
    public ResponseEntity<String> changePassword(
            @Validated @RequestBody ChangePasswordDTO changePasswordDTO
    ) {
        return ResponseEntity.ok(userServiceImpl.changePassword(changePasswordDTO));
    }

}

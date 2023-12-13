package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.AuthenticationResponse;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.email.EmailService;
import com.amalitech.gpuconfigurator.service.otp.OtpService;
import com.amalitech.gpuconfigurator.service.user.UserService;
import com.amalitech.gpuconfigurator.service.user.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userServiceImpl;
    private final UserRepository repository;
    private final OtpService otpService;
    private final EmailService emailService;


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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request )  {
        return ResponseEntity.ok(userServiceImpl.login(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) throws MessagingException {

        userServiceImpl.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("Email with otp sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        userServiceImpl.verifyResetOtp(verifyOtpDTO);
        return ResponseEntity.ok("is verified"); // change to response dto
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword( // change to Change Password dto
            @Validated @RequestBody ChangePasswordResponseDTO changePasswordDTO
    ) {
        userServiceImpl.changePassword(changePasswordDTO);
        return ResponseEntity.ok("password changed successfully");
    }

}

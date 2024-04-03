package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.auth.*;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.dto.otp.VerifyOTPResponse;
import com.amalitech.gpuconfigurator.dto.otp.VerifyOtpDTO;
import com.amalitech.gpuconfigurator.dto.otp.VerifyUserDto;
import com.amalitech.gpuconfigurator.dto.profile.ChangePasswordDTO;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Operation(
            summary = "User signup",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/signup")
    public ResponseEntity<SignupResponse> signup(@Validated @RequestBody SignUpDto request) throws MessagingException, BadRequestException {
        userService.signup(request);

        SignupResponse message = new SignupResponse("Email Verification sent");


        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Verify user",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/verify")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerifyUserDto request,
            @RequestAttribute("userSession") UserSession userSession
    ) {

        AuthenticationResponse user = userService.verifyUserSignup(request, userSession);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Resend OTP",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/resend-otp")
    public ResponseEntity<GenericResponse> resendOtp(@RequestBody ResendOtpDto resend) throws MessagingException {
        GenericResponse response = userService.resendOtp(resend);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User login",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginDto request,
            @RequestAttribute("userSession") UserSession userSession
    ) {
        return ResponseEntity.ok(userService.login(request, userSession));
    }

    @Operation(
            summary = "Reset user password",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/reset-password")
    public ResponseEntity<ResetResponseDTO> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) throws MessagingException {
        userService.resetPassword(resetPasswordDto);
        ResetResponseDTO message = new ResetResponseDTO("Email with otp sent");
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Verify OTP for password reset",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        userService.verifyResetOtp(verifyOtpDTO);
        VerifyOTPResponse message = new VerifyOTPResponse("OTP verified successfully");
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Change user password",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/auth/change-password")
    public ResponseEntity<GenericResponse> changePassword(
            @Validated @RequestBody ChangePasswordDTO changePasswordDTO

    ) throws BadRequestException {
        GenericResponse changePasswordResponse = userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(changePasswordResponse);
    }

}

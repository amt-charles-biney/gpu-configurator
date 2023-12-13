package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.MailService;
import com.amalitech.gpuconfigurator.service.OtpService;
import com.amalitech.gpuconfigurator.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final OtpService otpService;
    private final MailService mailService;

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) {
        if (userRepository.existsByEmail(resetPasswordDto.getEmail())) {
            var otp = otpService.generateOtp(resetPasswordDto.getEmail(), OtpType.RESET);
            try {
                mailService.sendOtpMail(resetPasswordDto.getEmail(), otp);
                return ResponseEntity.ok(ResetPasswordResponseDTO
                        .builder()
                        .message("OTP for reset password sent to email successfully")
                        .build());
            } catch (MessagingException e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        if (otpService.verifyOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtpCode(), OtpType.RESET)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(
            @Validated @RequestBody ChangePasswordDTO changePasswordDTO
    ) {
        String email = changePasswordDTO.getEmail();
        String otpCode = changePasswordDTO.getOtpCode();
        var newPassword = changePasswordDTO.getNewPassword();
        var confirmNewPassword = changePasswordDTO.getConfirmNewPassword();

        if (!newPassword.equals(confirmNewPassword)) {
            var response = ChangePasswordResponseDTO
                    .builder()
                    .message("New password and confirmation do not match")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValidOtp = otpService.verifyOtp(email, otpCode, OtpType.RESET);
        if (!isValidOtp) {
            return ResponseEntity.badRequest().body(ChangePasswordResponseDTO
                    .builder()
                    .message("Invalid OTP")
                    .build());
        }
        otpService.deleteOtp(email, otpCode);
        if (userService.changePassword(email, changePasswordDTO.getNewPassword())) {
            return ResponseEntity.ok(ChangePasswordResponseDTO
                    .builder()
                    .message("Password was changed successfully")
                    .build());
        }
        return ResponseEntity.badRequest().build();
    }
}

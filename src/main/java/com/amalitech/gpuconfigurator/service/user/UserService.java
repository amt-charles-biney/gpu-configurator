package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import jakarta.mail.MessagingException;

public interface UserService {
    void signup(SignUpDto request) throws MessagingException;
    AuthenticationResponse login(LoginDto request);

    AuthenticationResponse verifyUserSignup(VerifyUserDto request);

    void changePassword(ChangePasswordResponseDTO changePasswordDTO);

    void verifyResetOtp(VerifyOtpDTO verifyOtpDTO);

    void resetPassword(ResetPasswordDTO resetPasswordDto) throws MessagingException;
}

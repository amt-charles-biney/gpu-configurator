package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import com.amalitech.gpuconfigurator.service.jwt.JwtServiceImpl;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final OtpServiceImpl otpService;
    private final EmailServiceImpl emailService;

    @Override
    public void signup(SignUpDto request) throws MessagingException {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isVerified(false)
                .build();

        repository.save(user);
        String code = otpService.generateAndSaveOtp(user, OtpType.CREATE);
        emailService.sendOtpMessage(user.getEmail(), code, OtpType.CREATE);
    }

    @Override
    public AuthenticationResponse login(LoginDto request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("Email or Password incorrect"));
        if(!user.getIsVerified()) {
            throw new UsernameNotFoundException("email not verified");
        }
        var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    @Override
    public AuthenticationResponse verifyUserSignup(VerifyUserDto request) {
        boolean isValidOtp = otpService.isValidOtp(request.email(), request.code(), OtpType.CREATE);
        if(!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }

        User user = repository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("user does not exist"));
        user.setIsVerified(true);

        repository.save(user);

        var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws MessagingException {
        User user = repository.findByEmail(resetPasswordDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("we have sent you an email"));

        var otp = otpService.generateAndSaveOtp(user, OtpType.RESET);

        emailService.sendOtpMessage(user.getEmail(), otp, OtpType.RESET);
    }

    public void verifyResetOtp(VerifyOtpDTO verifyOtpDTO) {
        boolean isValidOtp = otpService.isValidOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtpCode(), OtpType.RESET);
        if(!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }
    }

    public void changePassword(ChangePasswordResponseDTO changePasswordResponseDTO) {
        //        String email = changePasswordDTO.getEmail();
//        String otpCode = changePasswordDTO.getOtpCode();
//        var newPassword = changePasswordDTO.getNewPassword();
//        var confirmNewPassword = changePasswordDTO.getConfirmNewPassword();
//
//        if (!newPassword.equals(confirmNewPassword)) {
//            var response = ChangePasswordResponseDTO
//                    .builder()
//                    .message("New password and confirmation do not match")
//                    .build();
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        boolean isValidOtp = otpService.verifyOtp(email, otpCode, OtpType.RESET);
//        if (!isValidOtp) {
//            return ResponseEntity.badRequest().body(ChangePasswordResponseDTO
//                    .builder()
//                    .message("Invalid OTP")
//                    .build());
//        }
//        otpService.deleteOtp(email, otpCode);
//        if (userServiceImpl.changePassword(email, changePasswordDTO.getNewPassword())) {
//            return ResponseEntity.ok(ChangePasswordResponseDTO
//                    .builder()
//                    .message("Password was changed successfully")
//                    .build());
//        }
    }



}

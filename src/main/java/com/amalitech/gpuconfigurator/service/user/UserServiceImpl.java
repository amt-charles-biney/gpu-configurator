package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.auth.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.auth.LoginDto;
import com.amalitech.gpuconfigurator.dto.auth.ResetPasswordDTO;
import com.amalitech.gpuconfigurator.dto.auth.SignUpDto;
import com.amalitech.gpuconfigurator.dto.email.OtpTemplate;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.dto.otp.VerifyOtpDTO;
import com.amalitech.gpuconfigurator.dto.otp.VerifyUserDto;
import com.amalitech.gpuconfigurator.dto.profile.ChangePasswordDTO;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.cart.CartService;
import com.amalitech.gpuconfigurator.service.email.OtpEmailServiceImpl;
import com.amalitech.gpuconfigurator.service.jwt.JwtServiceImpl;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import com.amalitech.gpuconfigurator.service.wishlist.WishListService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final OtpServiceImpl otpService;
    private final OtpEmailServiceImpl emailService;
    private final CartService cartService;
    private final WishListService wishListService;

    @Override
    public void signup(SignUpDto request) throws MessagingException, BadRequestException {

      Optional<User> newUser = repository.findByEmail(request.getEmail());
      if(newUser.isPresent()){
          throw new BadRequestException("Email Already Exist");
      }

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
        OtpTemplate otpTemplate = OtpTemplate
                .builder()
                .to(user.getEmail())
                .otp(code)
                .otpType(OtpType.CREATE)
                .build();

        emailService.send(otpTemplate);
    }

    @Override
    public AuthenticationResponse login(LoginDto request, UserSession userSession) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password incorrect"));
        if (!user.getIsVerified()) {
            throw new UsernameNotFoundException("email not verified");
        }

        Map<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("role", user.getRole());

        var jwtToken = jwtServiceImpl.generateToken(extraClaim, user);

        cartService.mergeUserAndSessionCarts(user, userSession);

        wishListService.mergeUserAndSessionWishLists(user, userSession);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(String.valueOf(user.getRole()))
                .build();
    }


    @Override
    public AuthenticationResponse verifyUserSignup(VerifyUserDto request, UserSession userSession) {
        boolean isValidOtp = otpService.isValidOtp(request.email(), request.code(), OtpType.CREATE);
        if (!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }

        User user = repository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("user does not exist"));
        user.setIsVerified(true);

        repository.save(user);

        Map<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("role", user.getRole());

        var jwtToken = jwtServiceImpl.generateToken(extraClaim, user);

        cartService.mergeUserAndSessionCarts(user, userSession);

        wishListService.mergeUserAndSessionWishLists(user, userSession);

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

        OtpTemplate otpTemplate = OtpTemplate
                .builder()
                .otpType(OtpType.RESET)
                .to(resetPasswordDTO.getEmail())
                .otp(otp)
                .build();

        emailService.send(otpTemplate);
    }

    public void verifyResetOtp(VerifyOtpDTO verifyOtpDTO) {
        boolean isValidOtp = otpService.isValidOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtpCode(), OtpType.RESET);
        if (!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }
    }

    public GenericResponse changePassword(ChangePasswordDTO changePasswordDTO) throws BadRequestException {
        User user = repository.findByEmail(changePasswordDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("cannot change password, try again"));
        Boolean otpValid = otpService.isValidOtp(changePasswordDTO.getEmail(), changePasswordDTO.getOtpCode(), OtpType.RESET);
        if(!otpValid) {
            throw new BadRequestException("could not change password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.newPassword));
        repository.save(user);
        return new GenericResponse(201, "user changed password successfully");
    }

    public GenericResponse resendOtp(ResendOtpDto resend) throws MessagingException {
        User user = repository.findByEmail(resend.email()).orElseThrow(() -> new UsernameNotFoundException("could not send otp as this user does not exist"));
        String otp = otpService.generateAndSaveOtp(user, OtpType.valueOf(resend.type()));

        OtpTemplate otpTemplate = OtpTemplate
                .builder()
                .otp(otp)
                .otpType(OtpType.valueOf(resend.type()))
                .to(user.getEmail())
                .build();

        emailService.send(otpTemplate);

        return new GenericResponse(201, "otp has been sent");
    }

}

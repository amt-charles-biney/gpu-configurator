package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.auth.LoginDto;
import com.amalitech.gpuconfigurator.dto.auth.SignUpDto;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.dto.otp.VerifyUserDto;
import com.amalitech.gpuconfigurator.dto.profile.ChangePasswordDTO;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import com.amalitech.gpuconfigurator.model.enums.Role;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import com.amalitech.gpuconfigurator.service.jwt.JwtServiceImpl;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import com.amalitech.gpuconfigurator.service.user.UserServiceImpl;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private OtpServiceImpl otpService;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldBeSuccessfulSignup() throws MessagingException, BadRequestException {
        SignUpDto signUpDto = new SignUpDto("Dickson", "Anyaele", "dickson.anyaele@gmail.com", "password");

        when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.ofNullable(null));
        userService.signup(signUpDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldBeFailedSignup() throws MessagingException, BadRequestException {
        SignUpDto signUpDto = new SignUpDto("Dickson", "Anyaele", "dickson.anyaele@gmail.com", "password");
        User existingUser = new User();

        when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(BadRequestException.class, () -> userService.signup(signUpDto));
    }


    @Test
    public void testVerifyUserSignupSuccess() {

            VerifyUserDto verifyUserDto = new VerifyUserDto("dickson.anyaele@gmail.com", "123456");
            User user = new User();
            user.setEmail("john.doe@example.com");
            user.setIsVerified(false);

            when(userRepository.findByEmail(verifyUserDto.email())).thenReturn(Optional.of(user));
            when(otpService.isValidOtp(verifyUserDto.email(), verifyUserDto.code(), OtpType.CREATE)).thenReturn(true);

            AuthenticationResponse response = userService.verifyUserSignup(verifyUserDto);

            assertNotNull(response);
            assertTrue(user.getIsVerified());

    }

    @Test
    public void testVerifyUserSignupFailure() {
        VerifyUserDto verifyUserDto = new VerifyUserDto("dickson.anyaele@gmail.com", "123456");

        when(userRepository.findByEmail(verifyUserDto.email())).thenReturn(Optional.empty());
        when(otpService.isValidOtp(verifyUserDto.email(), verifyUserDto.code(), OtpType.CREATE)).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> userService.verifyUserSignup(verifyUserDto));
    }

    @Test
    public void testLoginSuccess() {
        LoginDto loginDto = new LoginDto("dickson.anyaele@gmail.com", "password");
        User user = new User();
        user.setEmail("dickson.anyaele@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setIsVerified(true);

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));

        AuthenticationResponse response = userService.login(loginDto);

        assertNotNull(response);
        assertEquals(loginDto.getEmail(), response.getEmail());
    }

    @Test
    public void testLoginFailure() {
        LoginDto loginDto = new LoginDto("dickson.anyaele@gmail.com", "password");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(loginDto));
    }


    @Test
    public void testChangePasswordSuccess() throws BadRequestException {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("john.doe@example.com", "123456", "newPassword123", "newPassword123");
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(changePasswordDTO.getEmail())).thenReturn(Optional.of(user));
        when(otpService.isValidOtp(changePasswordDTO.getEmail(), changePasswordDTO.getOtpCode(), OtpType.RESET)).thenReturn(true);

        GenericResponse response = userService.changePassword(changePasswordDTO);

        assertNotNull(response);
        assertEquals(201, response.status());
        assertEquals("user changed password successfully", response.message());
    }

    @Test
    public void testChangePasswordInvalidOtp() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("john.doe@example.com", "123456", "newPassword123", "newPassword123");
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(changePasswordDTO.getEmail())).thenReturn(Optional.of(user));
        when(otpService.isValidOtp(changePasswordDTO.getEmail(), changePasswordDTO.getOtpCode(), OtpType.RESET)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.changePassword(changePasswordDTO));
    }

    @Test
    public void testChangePasswordUserNotFound() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("john.doe@example.com", "123456", "newPassword123", "newPassword123");

        when(userRepository.findByEmail(changePasswordDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.changePassword(changePasswordDTO));
    }

    @Test
    public void testResendOtpSuccess() throws MessagingException {
        ResendOtpDto resendOtpDto = new ResendOtpDto("john.doe@example.com", "RESET");
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(resendOtpDto.email())).thenReturn(Optional.of(user));
        when(otpService.generateAndSaveOtp(user, OtpType.RESET)).thenReturn("123456");

        GenericResponse response = userService.resendOtp(resendOtpDto);

        assertNotNull(response);
        assertEquals(201, response.status());
        assertEquals("otp has been sent", response.message());
    }

    @Test
    public void testResendOtpUserNotFound() {
        ResendOtpDto resendOtpDto = new ResendOtpDto("john.doe@example.com", "RESET");

        when(userRepository.findByEmail(resendOtpDto.email())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.resendOtp(resendOtpDto));
    }


}

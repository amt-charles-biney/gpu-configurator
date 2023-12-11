package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.controller.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.UserDto;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user-related operations such as user registration.
 * This class provides methods to handle user sign-up and generate authentication tokens.
 *
 * @author Clement Owireku-Bogyah
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * The password encoder for encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The JWT service for generating authentication tokens.
     */
    private final JwtService jwtService;

    /**
     * Signs up a new user using the provided user information.
     *
     * @param request The user information for sign-up.
     * @return An AuthenticationResponse containing the user's information and authentication token.
     */
    public AuthenticationResponse signup(UserDto request) {
        // Create a new user with encoded password and default role (USER)
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}

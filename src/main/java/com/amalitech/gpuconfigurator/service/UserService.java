package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.controller.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.LoginDto;
import com.amalitech.gpuconfigurator.dto.SignUpDto;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse signup(SignUpDto request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isVerified(false)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public AuthenticationResponse login(LoginDto request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("Email or Password incorrect"));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


}

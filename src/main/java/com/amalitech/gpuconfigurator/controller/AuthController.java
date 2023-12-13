package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.LoginDto;
import com.amalitech.gpuconfigurator.dto.SignUpDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userServiceImpl;
    private final UserRepository repository;
    @PostMapping("/v1/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse signup(@RequestBody SignUpDto request){
    return userServiceImpl.signup(request);
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request )  {
        return ResponseEntity.ok(userServiceImpl.login(request));
    }

}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.UserDto;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
//    private final UserRepository repository;
    private final UserService userService;
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse signup(@Valid @RequestBody UserDto request){

    return userService.signup(request);
    }

}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.LoginDto;
import com.amalitech.gpuconfigurator.dto.SignUpDto;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final UserRepository repository;
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse signup(@Validated @RequestBody  SignUpDto request){
    return userService.signup(request);
    }

    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request )  {
        return ResponseEntity.ok(userService.login(request));
    }


    //todo : for testing purpose :  Remove it later
    @GetMapping("/users")
    public List<Iterable<User>> users(){
        return List.of(repository.findAll());
    }

}

package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.controller.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.LoginDto;
import com.amalitech.gpuconfigurator.dto.SignUpDto;

public interface IUserService {
    AuthenticationResponse signup(SignUpDto request);
    AuthenticationResponse login(LoginDto request);
}

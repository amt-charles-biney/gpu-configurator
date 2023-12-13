package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.LoginDto;
import com.amalitech.gpuconfigurator.dto.SignUpDto;

public interface UserService {
    AuthenticationResponse signup(SignUpDto request);
    AuthenticationResponse login(LoginDto request);
}
